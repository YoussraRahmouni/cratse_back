package fr.tse.poc.controller;

import fr.tse.poc.dao.RoleRepository;
import fr.tse.poc.dao.UserRepository;
import fr.tse.poc.domain.Role;
import fr.tse.poc.domain.User;
import fr.tse.poc.security.jwt.JwtUtils;
import fr.tse.poc.security.services.UserDetailsImpl;
import fr.tse.poc.utils.LoginRequest;
import fr.tse.poc.utils.MessageResponse;
import fr.tse.poc.utils.SignupRequest;
import fr.tse.poc.utils.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static fr.tse.poc.utils.Constantes.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginBody){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginBody.getEmail(), loginBody.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getIdUser(),
                        userDetails.getFirstName(),
                        userDetails.getLastName(),
                        userDetails.getEmail(),
                        role));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (Boolean.TRUE.equals(userRepo.existsByEmail(signUpRequest.getEmail()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setJob(signUpRequest.getJob());

        Long role = signUpRequest.getRole();
        Role roleBD;

        if (role == null) {
            throw new RuntimeException(EXCEPTION_ROLE_NOT_FOUND);
        } else {

            if (role == ADMIN_ROLE) {
                roleBD = roleRepo.findById(ADMIN_ROLE)
                        .orElseThrow(() -> new RuntimeException(EXCEPTION_ROLE_NOT_FOUND));
            } else if (role == MANAGER_ROLE) {
                roleBD = roleRepo.findById(MANAGER_ROLE)
                        .orElseThrow(() -> new RuntimeException(EXCEPTION_ROLE_NOT_FOUND));
            } else if (role == USER_ROLE) {
                roleBD = roleRepo.findById(USER_ROLE)
                        .orElseThrow(() -> new RuntimeException(EXCEPTION_ROLE_NOT_FOUND));
            } else {
                throw new RuntimeException(EXCEPTION_ROLE_NOT_FOUND);
            }
        }

        user.setRole(roleBD);
        userRepo.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
