package fr.tse.poc.controller;

import fr.tse.poc.domain.Imputation;
import fr.tse.poc.domain.Project;
import fr.tse.poc.domain.User;
import fr.tse.poc.service.ImputationService;
import fr.tse.poc.service.ProjectService;
import fr.tse.poc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ImputationController {

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    ImputationService imputationService;

    @GetMapping("users/{userId}/imputations/projects")
    public ResponseEntity<List<Project>> getProjects(@PathVariable(value = "userId") Long userId,
                                                                  Principal principal){
        User user = this.userService.checkUserExists(userId);
        this.userService.checkRole(principal, user);
        List<Project> projects = this.imputationService.findProjectsByUser(user);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/imputations")
    public ResponseEntity<List<Imputation>> getAllImputations(@PathVariable(value = "userId") Long userId,
                                                              Principal principal){
        User user = this.userService.checkUserExists(userId);
        this.userService.checkRole(principal, user);

        List<Imputation> imputations = this.imputationService.findAllImputations(user);
        return new ResponseEntity<>(imputations, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/projects/{projectId}/imputations")
    public ResponseEntity<List<Imputation>> getProjectImputations(@PathVariable(value = "userId") Long userId,
                                                              @PathVariable(value = "projectId") Long projectId,
                                                              Principal principal){

        User user = this.userService.checkUserExists(userId);
        Project project = this.projectService.checkProjectExists(projectId);

        this.userService.checkRole(principal, user);

        List<Imputation> imputations = this.imputationService.findProjectImputations(user, project);
        return new ResponseEntity<>(imputations, HttpStatus.OK);

    }

    @PutMapping(value = "/users/{userId}/projects/{projectId}/imputations", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Imputation> createImputation(@PathVariable(value = "userId") Long userId,
                                                       @PathVariable(value = "projectId") Long projectId,
                                                       @Valid @RequestBody Imputation imputation,
                                                       Principal principal){
        User user = this.userService.checkUserExists(userId);
        Project project = this.projectService.checkProjectExists(projectId);
        this.userService.checkUserId(principal, user);
        Imputation imputationBD = this.imputationService.findImputationByUserAndProjectAndDate(user, project, imputation.getDateImputation());
        if(imputationBD == null){
            imputationBD = new Imputation();
            imputationBD.setUser(user);
            imputationBD.setProject(project);
            imputationBD.setDateImputation(imputation.getDateImputation());
        }
        imputationBD.setDailyChargeImputation(imputation.getDailyChargeImputation());
        Imputation newImp = this.imputationService.createImputation(imputationBD);
        return new ResponseEntity<>(newImp, HttpStatus.CREATED);

    }

}
