package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserDao;
import org.example.domain.User;
import org.example.exception.UserAlreadyExistsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserDao dao;


    @Override
    @Transactional
    public User insert(User user) {
        if(dao.findByLogin(user.getLogin()).isPresent())
            throw new UserAlreadyExistsException("User with login " + user.getLogin() +
                    " already exists");
        return dao.save(User.builder().login(user.getLogin()).password(new BCryptPasswordEncoder(
                10).encode(user.getPassword())).roles(user.getRoles()).build());
    }

    @Override
    @Transactional(readOnly = true)
    public User findByLogin(String login) {
        return dao.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(
                "User with login " + login + " was not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByRoleName(String roleName) {
        return dao.findByRolesName(roleName);
    }
}
