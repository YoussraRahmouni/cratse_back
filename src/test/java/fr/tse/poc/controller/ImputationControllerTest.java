package fr.tse.poc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.tse.poc.domain.Imputation;
import fr.tse.poc.domain.Project;
import fr.tse.poc.domain.User;
import fr.tse.poc.service.ImputationService;
import fr.tse.poc.service.ProjectService;
import fr.tse.poc.service.UserService;
import fr.tse.poc.utils.LoginRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ImputationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImputationService imputationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @BeforeEach
    public void setup() throws Exception {
        LoginRequest loginBody = new LoginRequest();
        loginBody.setEmail("john@gmail.com");
        loginBody.setPassword("poc");
        ObjectMapper mapper = new ObjectMapper();
        byte[] loginBodyAsBytes = mapper.writeValueAsBytes(loginBody);
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(loginBodyAsBytes))
                .andExpect(status().isOk());
    }

    @Test
    void createImputationUnauthorized() throws Exception {
        Imputation imp = new Imputation();
        imp.setDateImputation(LocalDate.of(2023, 2, 7));
        imp.setDailyChargeImputation(0.4);
        ObjectMapper mapper = new ObjectMapper();
        byte[] impAsBytes = mapper.writeValueAsBytes(imp);
        mockMvc.perform(post("/users/8/projects/12/imputations")
                .contentType(MediaType.APPLICATION_JSON).content(impAsBytes))
                .andExpect(status().isUnauthorized());
    }

//    @Test
//    @WithMockUser(username="john@gmail.com", roles = "Manager")
//    void createImputationAuthorized() throws Exception {
//        Imputation imp = new Imputation();
//        imp.setDateImputation(LocalDate.of(2023, 2, 7));
//        imp.setDailyChargeImputation(0.4);
//        User user = this.userService.findUser(8);
//        imp.setUser(user);
//        Project project = this.projectService.findProject(12);
//        imp.setProject(project);
//        ObjectMapper mapper = new ObjectMapper();
//        byte[] impAsBytes = mapper.writeValueAsBytes(imp);
//        mockMvc.perform(put("/users/8/projects/12/imputations"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("user.idUser", is(8)));
//
//        List<Imputation> list =  this.imputationService.findAllImputations(user);
//        Assertions.assertEquals(5, list.size());
//    }

    @Test
    @WithMockUser(username="john@gmail.com", roles = "Manager")
    void getProjectsOfUserAuthorized() throws Exception {
        mockMvc.perform(get("/users/8/imputations/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProject", is(3)))
                .andExpect(jsonPath("$[0].nameProject", is("Infocable")));
    }

    @Test
    @WithMockUser(username="john@gmail.com", roles = "Manager")
    void getProjectsOfUserForbidden() throws Exception {
        mockMvc.perform(get("/users/10/imputations/projects"))
                .andExpect(status().isForbidden());
    }

}
