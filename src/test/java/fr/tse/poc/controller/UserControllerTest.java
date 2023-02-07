package fr.tse.poc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.tse.poc.utils.LoginRequest;
import fr.tse.poc.utils.UpdateManagerRequest;
import fr.tse.poc.utils.UpdateRoleRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        LoginRequest loginBody = new LoginRequest();
        loginBody.setEmail("admin@gmail.com");
        loginBody.setPassword("poc");
        ObjectMapper mapper = new ObjectMapper();
        byte[] loginBodyAsBytes = mapper.writeValueAsBytes(loginBody);
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(loginBodyAsBytes))
                .andExpect(status().isOk());
    }

    @Test
    void getUsersUnauthorized() throws Exception {
        mockMvc.perform(get("/users")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="admin@gmail.com", roles = "Admin")
    void getUsersAuthorized() throws Exception {
        mockMvc.perform(get("/users")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUser", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Manal")))
                .andExpect(jsonPath("$[0].lastName", is("DERMICH")));
    }

    @Test
    void getManagersUnauthorized() throws Exception {
        mockMvc.perform(get("/managers")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="admin@gmail.com", roles = "Admin")
    void getManagersAuthorized() throws Exception {
        mockMvc.perform(get("/managers")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUser", is(2)))
                .andExpect(jsonPath("$[0].firstName", is("Youssra")))
                .andExpect(jsonPath("$[0].lastName", is("RAHMOUNI")));
    }

    @Test
    void updateUserRoleUnauthorized() throws Exception {
        UpdateRoleRequest updateRoleReq = new UpdateRoleRequest();
        updateRoleReq.setRole(2L);
        ObjectMapper mapper = new ObjectMapper();
        byte[] updateRoleReqAsBytes = mapper.writeValueAsBytes(updateRoleReq);
        mockMvc.perform(put("/users/9/role")
                .contentType(MediaType.APPLICATION_JSON).content(updateRoleReqAsBytes))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="admin@gmail.com", roles = "Admin")
    void updateUserRoleAuthorized() throws Exception {
        UpdateRoleRequest updateRoleReq = new UpdateRoleRequest();
        updateRoleReq.setRole(2L);
        ObjectMapper mapper = new ObjectMapper();
        byte[] updateRoleReqAsBytes = mapper.writeValueAsBytes(updateRoleReq);
        mockMvc.perform(put("/users/9/role")
                .contentType(MediaType.APPLICATION_JSON).content(updateRoleReqAsBytes))
                .andExpect(status().isOk())
                .andExpect(jsonPath("idUser", is(9)))
                .andExpect(jsonPath("role.labelRole", is("Manager")));
    }

    @Test
    void updateUserManagerUnauthorized() throws Exception {
        UpdateManagerRequest updateManagerReq = new UpdateManagerRequest();
        updateManagerReq.setManager(8L);
        ObjectMapper mapper = new ObjectMapper();
        byte[] updateManagerReqAsBytes = mapper.writeValueAsBytes(updateManagerReq);
        mockMvc.perform(put("/users/9/manager")
                .contentType(MediaType.APPLICATION_JSON).content(updateManagerReqAsBytes))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="admin@gmail.com", roles = "Admin")
    void updateUserManagerAuthorized() throws Exception {
        UpdateManagerRequest updateManagerReq = new UpdateManagerRequest();
        updateManagerReq.setManager(8L);
        ObjectMapper mapper = new ObjectMapper();
        byte[] updateManagerReqAsBytes = mapper.writeValueAsBytes(updateManagerReq);
        mockMvc.perform(put("/users/9/manager")
                .contentType(MediaType.APPLICATION_JSON).content(updateManagerReqAsBytes))
                .andExpect(status().isOk())
                .andExpect(jsonPath("idUser", is(9)))
                .andExpect(jsonPath("manager.idUser", is(8)));
    }

}
