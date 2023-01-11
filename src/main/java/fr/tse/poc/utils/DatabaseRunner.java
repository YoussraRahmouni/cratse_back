package fr.tse.poc.utils;

import fr.tse.poc.dao.RoleRepository;
import fr.tse.poc.dao.UserRepository;
import fr.tse.poc.domain.Role;
import fr.tse.poc.domain.User;
import fr.tse.poc.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseRunner implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    private RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        Role roleAdmin = new Role();
        roleAdmin.setLabelRole("Admin");

        Role roleManager = new Role();
        roleManager.setLabelRole("Manager");

        Role roleUser = new Role();
        roleUser.setLabelRole("User");

        roleRepo.save(roleAdmin);
        roleRepo.save(roleManager);
        roleRepo.save(roleUser);

        User user1 = new User();
        user1.setEmail("mdermich@gmail.com");
        user1.setPassword("poc");
        user1.setFirstName("Manal");
        user1.setLastName("DERMICH");
        user1.setJob("Developper");
        Role adminRole = roleService.findRole(Constantes.ADMIN_ROLE);
        user1.setRole(adminRole);
        userRepo.save(user1);

        User user2 = new User();
        user2.setEmail("youssra@gmail.com");
        user2.setPassword("poc");
        user2.setFirstName("Youssra");
        user2.setLastName("RAHMOUNI");
        user2.setJob("Developper");
        Role managerRole = roleService.findRole(Constantes.MANAGER_ROLE);
        user2.setRole(managerRole);
        userRepo.save(user2);
    }
}
