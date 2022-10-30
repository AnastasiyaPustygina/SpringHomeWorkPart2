package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.example.domain.User;
import org.example.rest.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Класс UserController")
public class UserControllerTest {
    private static final int COUNT_OF_FIRST_ROLE_USERS = 1;
    private static final String EXISTING_ROLE_NAME = "reader";
    private static final String EXISTING_USER_LOGIN = "log";
    private static final String NEW_USER_LOGIN = "newLog";
    private static final String NEW_USER_PASSWORD= "newPass";
    private static final long NEW_USER_ID= 3;
    private static final String EXISTING_USER_PASS = "pass";
    private static final long EXISTING_USER_ID = 1;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    @DisplayName("должен найти пользователей по роли")
    void shouldFindUsersByRoleName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/role/" + EXISTING_ROLE_NAME))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(COUNT_OF_FIRST_ROLE_USERS)));
    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    @DisplayName("должен найти пользователя по логину")
    void shouldFindUsersByLogin() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/user/" + EXISTING_USER_LOGIN))
                .andExpect(status().isOk()).andReturn();
        assertThat(new Gson().fromJson(result.getResponse().getContentAsString(), User.class))
                .usingRecursiveComparison().ignoringFields("password", "roles").isEqualTo(
                        User.builder().id(EXISTING_USER_ID).login(EXISTING_USER_LOGIN).roles(Collections.emptyList()).build());

    }

    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @Test
    @DisplayName("должен добавить пользователя")
    void shouldInsertAuthor() throws Exception {
        User user = User.builder().login(NEW_USER_LOGIN).password(NEW_USER_PASSWORD).roles(Collections.emptyList()).build();
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/user").contentType(APPLICATION_JSON).content(
                        mapper.writeValueAsString(UserDto.toDto(user)))).andExpect(status().isOk())
                .andReturn();
        assertThat(new Gson().fromJson(result.getResponse().getContentAsString(), User.class))
                .usingRecursiveComparison().ignoringFields("password").isEqualTo(
                        User.builder().id(NEW_USER_ID).login(NEW_USER_LOGIN).roles(Collections.emptyList()).build());
    }
}
