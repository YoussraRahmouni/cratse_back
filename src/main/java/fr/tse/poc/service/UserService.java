package fr.tse.poc.service;

import fr.tse.poc.dao.UserRepository;
import fr.tse.poc.domain.Role;
import fr.tse.poc.domain.User;
import fr.tse.poc.exceptions.NotAuthorizedException;
import fr.tse.poc.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    /**
     * Returns the User by its id
     * @param idUser the id of the user
     * @return the user if it exists, else return null
     */
    public User findUser(long idUser){
        return userRepo.findById(idUser).orElse(null);
    }

    public User findUser(String email){
        return userRepo.findByEmail(email).orElse(null);
    }

    public List<User> getAllUsers(){
        return this.userRepo.findAll();
    }

    public User checkUserExists(Object userIdOrEmail){
        User user;
        if(userIdOrEmail instanceof Long){
            user = this.findUser((Long) userIdOrEmail);
        }
        else if(userIdOrEmail instanceof String){
            user = this.findUser((String) userIdOrEmail);
        }
        else{
            throw new ResourceNotFoundException("Please provide a correct id (Long) or email (String)");
        }
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id or email = " + userIdOrEmail);
        }
        return user;
    }

    public User updateUserRole(User user, Role role){
        user.setRole(role);
        return this.userRepo.save(user);
    }

    public User updateUserManager(User user, User manager){
        user.setManager(manager);
        return this.userRepo.save(user);
    }

    public List<User> getUsersByManager(User manager){
        return this.userRepo.findByManager(manager);
    }

    public User createUser(User user){
        return this.userRepo.save(user);
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

}
