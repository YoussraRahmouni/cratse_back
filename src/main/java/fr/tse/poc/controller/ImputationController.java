package fr.tse.poc.controller;

import fr.tse.poc.domain.Imputation;
import fr.tse.poc.domain.Project;
import fr.tse.poc.domain.User;
import fr.tse.poc.exceptions.NotAuthorizedException;
import fr.tse.poc.exceptions.ResourceNotFoundException;
import fr.tse.poc.service.ImputationService;
import fr.tse.poc.service.ProjectService;
import fr.tse.poc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
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
        checkRole(principal, user);
        List<Project> projects = this.imputationService.findProjectsByUser(user);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/imputations")
    public ResponseEntity<List<Imputation>> getAllImputations(@PathVariable(value = "userId") Long userId,
                                                              Principal principal){
        User user = this.userService.checkUserExists(userId);
        checkRole(principal, user);

        List<Imputation> imputations = this.imputationService.findAllImputations(user);
        return new ResponseEntity<>(imputations, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/projects/{projectId}/imputations")
    public ResponseEntity<List<Imputation>> getProjectImputations(@PathVariable(value = "userId") Long userId,
                                                              @PathVariable(value = "projectId") Long projectId,
                                                              Principal principal){

        User user = this.userService.checkUserExists(userId);
        Project project = checkProjectExists(projectId);

        checkRole(principal, user);

        List<Imputation> imputations = this.imputationService.findProjectImputations(user, project);
        return new ResponseEntity<>(imputations, HttpStatus.OK);

    }

    @PutMapping(value = "/users/{userId}/projects/{projectId}/imputations", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Imputation> createImputation(@PathVariable(value = "userId") Long userId,
                                                       @PathVariable(value = "projectId") Long projectId,
                                                       @Valid @RequestBody Imputation imputation,
                                                       Principal principal){
        User user = this.userService.checkUserExists(userId);
        Project project = checkProjectExists(projectId);
        checkUserId(principal, user);
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

    public Project checkProjectExists(Long projectId){
        Project project = this.projectService.findProject(projectId);
        if (project == null) {
            throw new ResourceNotFoundException("Project not found with id = " + projectId);
        }
        return project;
    }

    /**
     * Check the user authenticated to see if it's the same as the user in the URL
     * @param principal the current authenticated user
     * @param user the user whose resources we want to modify
     */
    public void checkUserId(Principal principal, User user){
        // A user can't create imputations of other users
        if(!user.getEmail().equals(principal.getName())){
            throw new NotAuthorizedException("You can only access and modify your resources");
        }
    }

    /**
     * Check if current authenticated user is manager of user whose resources we want to access
     * @param principal the current authenticated manager
     * @param user the user whose resources we want to access
     */
    public void checkUserManager(Principal principal, User user){
        if(user.getManager() == null || !user.getManager().getEmail().equals(principal.getName())){
            throw new NotAuthorizedException("As a manager, you can access only your users' imputations");
        }
    }

    public void checkRole(Principal principal, User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream().findFirst().map(GrantedAuthority::getAuthority).orElse("");
        switch (role){
            case "User":
                checkUserId(principal, user);
                break;
            case "Manager":
                // if the manager isn't trying to access his imputations
                // then we must check if he's the manager of the user whose imputations he's trying to access
                if(!user.getEmail().equals(principal.getName())) {
                    checkUserManager(principal, user);
                }
                break;
            default:
                break;
        }
    }

}
