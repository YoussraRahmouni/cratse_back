package fr.tse.poc.service;

import fr.tse.poc.dao.UserRepository;
import fr.tse.poc.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
