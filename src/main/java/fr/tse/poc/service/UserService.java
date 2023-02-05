package fr.tse.poc.service;

import fr.tse.poc.dao.UserRepository;
import fr.tse.poc.domain.Role;
import fr.tse.poc.domain.User;
import fr.tse.poc.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
