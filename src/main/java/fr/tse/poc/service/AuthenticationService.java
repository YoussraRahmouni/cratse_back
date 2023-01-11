package fr.tse.poc.service;

import fr.tse.poc.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    UserRepository userRepo;

//    public boolean authenticateUser(){
//
//    }
}
