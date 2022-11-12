package org.example.dao;

import org.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "user")
public interface UserDao extends JpaRepository<User, Long> {

    @RestResource(path = "role/name", rel = "roleName")
    List<User> findByRolesName(String name);

    @RestResource(path = "login", rel = "login")
    Optional<User> findByLogin(String login);

}
