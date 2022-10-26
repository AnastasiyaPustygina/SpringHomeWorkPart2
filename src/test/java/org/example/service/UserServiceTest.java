package org.example.service;

import org.example.dao.UserDao;
import org.example.domain.User;
import org.example.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("Класс UserService")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {
    private static final long EXISTING_USER_ID = 1;
    private static final int COUNT_OF_FIRST_ROLE_USERS = 1;
    private static final String EXISTING_ROLE_NAME = "reader";
    private static final String EXISTING_USER_LOGIN = "log";
    private static final String NEW_USER_LOGIN = "newLog";
    private static final String NEW_USER_PASSWORD= "newPass";
    private static final long NEW_USER_ID= 3;
    private static final String EXISTING_USER_PASS = "pass";
    
    
    @Mock
    private UserDao dao;

    @InjectMocks
    private UserServiceImpl userService;

    @DisplayName("должен найти пользователя по логину")
    @Test
    void shouldFindUserByLogin(){
        User user = User.builder().id(EXISTING_USER_ID).login(EXISTING_USER_LOGIN)
                .password(EXISTING_USER_PASS).build();
        given(dao.findByLogin(EXISTING_USER_LOGIN)).willReturn(Optional.of(user));
        assertEquals(user, userService.findByLogin(EXISTING_USER_LOGIN));
    }

    @DisplayName("должен найти пользователей по роли")
    @Test
    void shouldFindUsersByRole(){
        List<User> users = List.of(User.builder().id(EXISTING_USER_ID).login(EXISTING_USER_LOGIN)
                .password(EXISTING_USER_PASS).build());
        given(dao.findByRolesName(EXISTING_ROLE_NAME)).willReturn(users);
        assertEquals(users, userService.findByRoleName(EXISTING_ROLE_NAME));
    }
    @DisplayName("должен добавить пользователя")
    @Test
    void shouldInsertGenre(){
        User user = User.builder().id(NEW_USER_ID).login(NEW_USER_LOGIN).password(NEW_USER_PASSWORD).roles(Collections.emptyList()).build();
        given(dao.save(any())).willReturn(user);
        given(dao.findByLogin(EXISTING_USER_LOGIN)).willReturn(Optional.of(User.builder().id(EXISTING_USER_ID).login(EXISTING_USER_LOGIN)
                .password(EXISTING_USER_PASS).build()));
        given(dao.findByLogin(NEW_USER_LOGIN)).willReturn(Optional.empty());
        assertAll(
                () -> assertEquals(userService.insert(user).getLogin(), user.getLogin()),
                () -> assertThrows(UserAlreadyExistsException.class, () -> {
                    userService.insert(User.builder().login(EXISTING_USER_LOGIN)
                            .password(EXISTING_USER_PASS).build());
                })
        );
    }
}
