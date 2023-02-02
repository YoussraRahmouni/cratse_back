package fr.tse.poc.controller;

import fr.tse.poc.domain.Imputation;
import fr.tse.poc.domain.Project;
import fr.tse.poc.domain.User;
import fr.tse.poc.exceptions.ResourceNotFoundException;
import fr.tse.poc.service.ImputationService;
import fr.tse.poc.service.ProjectService;
import fr.tse.poc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ImputationController {

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    ImputationService imputationService;

    @GetMapping("/users/{userId}/projects/{projectId}/imputations")
    public ResponseEntity<List<Imputation>> getAllImputations(@PathVariable(value = "userId") Long userId,
                                                              @PathVariable(value = "projectId") Long projectId){

        User user = checkUserExists(userId);
        Project project = checkProjectExists(projectId);

        List<Imputation> imputations = this.imputationService.findAllImputations(user, project);
        return new ResponseEntity<>(imputations, HttpStatus.OK);

    }

    @PostMapping("/users/{userId}/projects/{projectId}/imputations")
    public ResponseEntity<Imputation> createImputation(@PathVariable(value = "userId") Long userId,
                                                       @PathVariable(value = "projectId") Long projectId,
                                                       @Valid @RequestBody Imputation imputation){
        User user = checkUserExists(userId);
        Project project = checkProjectExists(projectId);
        imputation.setUser(user);
        imputation.setProject(project);
        Imputation newImp = this.imputationService.createImputation(imputation);
        return new ResponseEntity<>(newImp, HttpStatus.CREATED);
    }

    public User checkUserExists(Long userId){
        User user = this.userService.findUser(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id = " + userId);
        }
        return user;
    }

    public Project checkProjectExists(Long projectId){
        Project project = this.projectService.findProject(projectId);
        if (project == null) {
            throw new ResourceNotFoundException("Project not found with id = " + projectId);
        }
        return project;
    }

}
