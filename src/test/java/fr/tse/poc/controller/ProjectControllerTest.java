package fr.tse.poc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.tse.poc.domain.Project;
import fr.tse.poc.service.ProjectService;
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

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    void getAllProjectsUnauthorized() throws Exception {
        mockMvc.perform(get("/projects")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="john@gmail.com", roles = "Manager")
    void getAllProjectsAuthorized() throws Exception {
        mockMvc.perform(get("/projects")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProject", is(3)))
                .andExpect(jsonPath("$[0].nameProject", is("Infocable")))
                .andExpect(jsonPath("$[0].durationForecastProject", is(200.0)));
    }

    @Test
    void createProjectUnauthorized() throws Exception {
        Project project = new Project();
        project.setNameProject("Monitoring App");
        project.setDurationForecastProject(500.0);
        ObjectMapper mapper = new ObjectMapper();
        byte[] projectAsBytes = mapper.writeValueAsBytes(project);
        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON).content(projectAsBytes))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="john@gmail.com", roles = "Manager")
    void createProjectAuthorized() throws Exception {
        Project project = new Project();
        project.setNameProject("Monitoring App");
        project.setDurationForecastProject(500.0);
        ObjectMapper mapper = new ObjectMapper();
        byte[] projectAsBytes = mapper.writeValueAsBytes(project);
        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON).content(projectAsBytes))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("nameProject", is("Monitoring App")))
                .andExpect(jsonPath("durationForecastProject", is(500.0)));
        List<Project> allProjects = this.projectService.findAllProjects();
        Assertions.assertEquals(12, allProjects.size());
    }
}
