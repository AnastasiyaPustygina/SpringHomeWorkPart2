package org.example.dao;

import org.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {

    List<User> findByRolesName(String name);
    Optional<User> findByLogin(String login);

}
