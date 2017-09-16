package com.bairei.restspringboot;

import com.bairei.restspringboot.services.RoleService;
import com.bairei.restspringboot.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.isA;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
public class RestControllersTest {

    private static final Logger log = LoggerFactory.getLogger(RestControllersTest.class);

    @Autowired
    private MockMvc mvc;


    @Test
    public void adminGetPatients() throws Exception {
        this.mvc.perform(get("/patient").accept(MediaType.APPLICATION_JSON)
                .with(httpBasic("asd@oro.com", "asdasd")))
                .andExpect(status().isOk());
    }

    @Test
    public void userGetPatients() throws Exception {
        this.mvc.perform(get("/patient").accept(MediaType.APPLICATION_JSON)
                .with(httpBasic("jakismail@mail.pl", "pass")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void nonAuthorizedGetPatients() throws Exception {
        this.mvc.perform(get("/patient").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void adminPostPatient() throws Exception {

        String json =
                "{\"email\":\"mock@patient.com\"," +
                "\"name\":\"mock\"," +
                "\"surname\":\"patient\"," +
                "\"password\":\"mock\"," +
                "\"confirmPassword\":\"mock\"," +
                "\"secret\":\"\"," +
                "\"roles\":[{\"id\":1,\"name\":\"ROLE_USER\"}]}";

        this.mvc.perform(post("/patient")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json)
                .with(httpBasic("asd@oro.com","asdasd")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", isA(Integer.class)))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.confirmPassword").doesNotExist())
                .andExpect(jsonPath("$.secret").doesNotExist());

    }
}
