package fr.tse.poc.controller;

import fr.tse.poc.dao.RoleRepository;
import fr.tse.poc.dao.UserRepository;
import fr.tse.poc.domain.Role;
import fr.tse.poc.domain.User;
import fr.tse.poc.exceptions.ResourceNotFoundException;
import fr.tse.poc.security.jwt.JwtUtils;
import fr.tse.poc.security.services.UserDetailsImpl;
import fr.tse.poc.service.RoleService;
import fr.tse.poc.service.UserService;
import fr.tse.poc.utils.LoginRequest;
import fr.tse.poc.utils.MessageResponse;
import fr.tse.poc.utils.SignupRequest;
import fr.tse.poc.utils.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

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

        Role roleBD = this.roleService.findRoleByLabel(role);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getIdUser(),
                        userDetails.getFirstName(),
                        userDetails.getLastName(),
                        userDetails.getEmail(),
                        roleBD));
    }

    @PostMapping("/signup")
    @PreAuthorize("hasAnyAuthority('Manager')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, Principal principal) {

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
        // Get the authenticated manager's email
        String managerEmail = principal.getName();
        User manager = this.userRepo.findByEmail(managerEmail).orElse(null);
        // the current authenticated manager is the new user's manager
        user.setManager(manager);

        Long role = signUpRequest.getRole();
        Role roleBD;

        if (role == null) {
            throw new ResourceNotFoundException("Role not found");
        } else {
            roleBD = this.roleService.findRole(role);
        }

        user.setRole(roleBD);
        User newUser = this.userService.createUser(user);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = this.jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

}
