package org.example.service;

import org.example.domain.User;

import java.util.List;

public interface UserService {

    User insert(User user);

    User findByLogin(String login);

    List<User> findByRoleName(String roleName);
}
