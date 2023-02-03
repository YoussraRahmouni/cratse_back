package fr.tse.poc.controller;

import fr.tse.poc.domain.Role;
import fr.tse.poc.domain.User;
import fr.tse.poc.service.RoleService;
import fr.tse.poc.service.UserService;
import fr.tse.poc.utils.UpdateManagerRequest;
import fr.tse.poc.utils.UpdateRoleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @PreAuthorize("hasAnyAuthority('Admin')")
    @PutMapping("/users/{userId}/role")
    public ResponseEntity<User> updateUserRole(@PathVariable(value = "userId") Long userId, @Valid @RequestBody UpdateRoleRequest updateRoleRequest){
        User user = this.userService.checkUserExists(userId);
        Role role = this.roleService.findRole(updateRoleRequest.getRole());
        User updatedUser = this.userService.updateUserRole(user, role);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('Admin')")
    @PutMapping("/users/{userId}/manager")
    public ResponseEntity<User> updateUserManager(@PathVariable(value = "userId") Long userId, @Valid @RequestBody UpdateManagerRequest updateManagerRequest){
        User user = this.userService.checkUserExists(userId);
        User manager = this.userService.checkUserExists(updateManagerRequest.getManager());
        User updatedUser = this.userService.updateUserManager(user, manager);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

}
