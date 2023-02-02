package fr.tse.poc.utils;

import fr.tse.poc.dao.ImputationRepository;
import fr.tse.poc.dao.ProjectRepository;
import fr.tse.poc.dao.RoleRepository;
import fr.tse.poc.dao.UserRepository;
import fr.tse.poc.domain.Imputation;
import fr.tse.poc.domain.Project;
import fr.tse.poc.domain.Role;
import fr.tse.poc.domain.User;
import fr.tse.poc.service.ProjectService;
import fr.tse.poc.service.RoleService;
import fr.tse.poc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;

@Component
public class DatabaseRunner implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    ImputationRepository imputationRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ProjectService projectService;

    @Override
    public void run(String... args) throws Exception {
//        Role roleAdmin = new Role();
//        roleAdmin.setLabelRole("Admin");
//
//        Role roleManager = new Role();
//        roleManager.setLabelRole("Manager");
//
//        Role roleUser = new Role();
//        roleUser.setLabelRole("User");
//
//        roleRepo.save(roleAdmin);
//        roleRepo.save(roleManager);
//        roleRepo.save(roleUser);
//
//        User user1 = new User();
//        user1.setEmail("mdermich@gmail.com");
//        user1.setPassword("poc");
//        user1.setFirstName("Manal");
//        user1.setLastName("DERMICH");
//        user1.setJob("Developper");
//        Role adminRole = roleService.findRole(Constantes.ADMIN_ROLE);
//        user1.setRole(adminRole);
//        userRepo.save(user1);
//
//        User user2 = new User();
//        user2.setEmail("youssra@gmail.com");
//        user2.setPassword("poc");
//        user2.setFirstName("Youssra");
//        user2.setLastName("RAHMOUNI");
//        user2.setJob("Developper");
//        Role managerRole = roleService.findRole(Constantes.MANAGER_ROLE);
//        user2.setRole(managerRole);
//        userRepo.save(user2);

//        Project project1 = new Project();
//        project1.setNameProject("Infocable");
//        project1.setDurationForecastProject(200.0);
//        projectRepo.save(project1);
//
//        Project project2 = new Project();
//        project2.setNameProject("POC");
//        project2.setDurationForecastProject(150.0);
//        projectRepo.save(project2);
//
//        Imputation imputation1 = new Imputation();
//        imputation1.setDateImputation(LocalDate.of(2023, Month.JANUARY, 15));
//        imputation1.setDailyChargeImputation(0.5);
//        Project project = projectService.findProject(project1.getIdProject());
//        imputation1.setProject(project);
//        User user = userService.findUser("manal@gmail.com");
//        imputation1.setUser(user);
//        imputationRepo.save(imputation1);
    }
}
