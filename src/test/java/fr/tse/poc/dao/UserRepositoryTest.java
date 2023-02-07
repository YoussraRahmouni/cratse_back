package fr.tse.poc.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import fr.tse.poc.domain.Role;
import fr.tse.poc.domain.User;
import fr.tse.poc.service.RoleService;
import fr.tse.poc.utils.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleService roleService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("mdermich@gmail.com");
        user.setPassword("poc");
        user.setFirstName("Manal");
        user.setLastName("DERMICH");
        user.setJob("Developper");
        Role adminRole = roleService.findRole(Constantes.ADMIN_ROLE);
        user.setRole(adminRole);
        userRepo.save(user);
    }

    @Test
    void testUpdateUser() {
        user.setJob("Tester");
        userRepo.save(user);
        User updatedUser = userRepo.findById(user.getIdUser()).orElse(null);
        assert updatedUser != null;
        assertEquals(user.getJob(), updatedUser.getJob());
    }

    @Test
    void testDeleteUser() {
        userRepo.delete(user);
        User deletedUser = userRepo.findById(user.getIdUser()).orElse(null);
        assertNull(deletedUser);
    }
}
