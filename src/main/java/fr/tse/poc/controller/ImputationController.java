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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
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
                                                              @PathVariable(value = "projectId") Long projectId,
                                                              Principal principal){

        User user = this.userService.checkUserExists(userId);
        Project project = checkProjectExists(projectId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream().findFirst().map(GrantedAuthority::getAuthority).orElse("");
        switch (role){
            case "User":
                checkUserId(principal, user);
                break;
            case "Manager":
                checkUserManager(principal, user);
                break;
            default:
                break;
        }

        List<Imputation> imputations = this.imputationService.findAllImputations(user, project);
        return new ResponseEntity<>(imputations, HttpStatus.OK);

    }

    @PostMapping("/users/{userId}/projects/{projectId}/imputations")
    public ResponseEntity<Imputation> createImputation(@PathVariable(value = "userId") Long userId,
                                                       @PathVariable(value = "projectId") Long projectId,
                                                       @Valid @RequestBody Imputation imputation,
                                                       Principal principal){
        User user = this.userService.checkUserExists(userId);
        Project project = checkProjectExists(projectId);
        checkUserId(principal, user);
        imputation.setUser(user);
        imputation.setProject(project);
        Imputation newImp = this.imputationService.createImputation(imputation);
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
            throw new NotAuthorizedException("You can only access and modify your ressources");
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

}
