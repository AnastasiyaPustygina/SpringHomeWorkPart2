package org.example.rest.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.exception.UserAlreadyExistsException;
import org.example.rest.dto.UserDto;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/user/{login}")
    public UserDto findUserByLogin(@PathVariable(name = "login") String login){
        return UserDto.toDto(service.findByLogin(login));
    }

    @GetMapping("/user/role/{roleName}")
    public List<UserDto> findByRoleName(@PathVariable(name = "roleName") String roleName){
        return service.findByRoleName(roleName).stream().map(UserDto::toDto).toList();
    }


    @PostMapping("/user")
    public UserDto insertUser(@RequestBody UserDto userDto){
        User user = service.insert(UserDto.fromDto(userDto));
        return UserDto.toDto(user);
    }

    @ExceptionHandler({UserAlreadyExistsException.class, UsernameNotFoundException.class})
    public ResponseEntity<String> handlerUserException(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
