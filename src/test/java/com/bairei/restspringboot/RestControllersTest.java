package com.bairei.restspringboot;

import com.bairei.restspringboot.domain.User;
import com.bairei.restspringboot.services.RoleService;
import com.bairei.restspringboot.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
public class RestControllersTest {

    private static final Logger log = LoggerFactory.getLogger(RestControllersTest.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void adminGetPatients() throws Exception {
        this.mvc.perform(get("/patients").accept(MediaType.APPLICATION_JSON)
                .with(httpBasic("asd@oro.com", "asdasd")))
                .andExpect(status().isOk());
    }

    @Test
    public void userGetPatients() throws Exception {
        this.mvc.perform(get("/patients").accept(MediaType.APPLICATION_JSON)
                .with(httpBasic("jakismail@mail.pl", "pass")))
                .andExpect(status().isForbidden());
    }

    @Test
    public void nonAuthorizedGetPatients() throws Exception {
        this.mvc.perform(get("/patients").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void adminPostPatient() throws Exception {
//        User patient = new User();
//        patient.setName("mock");
//        patient.setSurname("patient");
//        patient.setEmail("mock@patient.com");
//        patient.setPassword("mock");
//        patient.setConfirmPassword("mock");
//        patient.setSecret("");
//        patient.setRoles(roleService.createUserRole());
//        ObjectMapper mapper = new ObjectMapper();

        String json =
                "{\"email\":\"mock@patient.com\"," +
                "\"name\":\"mock\"," +
                "\"surname\":\"patient\"," +
                "\"password\":\"mock\"," +
                "\"confirmPassword\":\"mock\"," +
                "\"secret\":\"\"," +
                "\"roles\":[{\"id\":1,\"name\":\"ROLE_USER\"}]}";

        log.info("JSON: " + json);

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
