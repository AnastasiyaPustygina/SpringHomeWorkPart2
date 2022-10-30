package org.example.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.domain.Role;
import org.example.domain.User;

import java.util.List;

@RequiredArgsConstructor
@Builder
@Getter
public class UserDto {

    private final long id;
    private final String login;
    private final String password;
    private final List<RoleDto> roles;

    public static UserDto toDto(User user){
        return UserDto.builder().id(user.getId()).login(user.getLogin()).password(user.getPassword())
                .roles(user.getRoles().stream().map(RoleDto::toDto).toList()).build();
    }

    public static User fromDto(UserDto userDto){
        return User.builder().id(userDto.getId()).login(userDto.getLogin()).password(userDto
                .getPassword()).roles(userDto.getRoles().stream().map(RoleDto::fromDto).toList()).build();
    }

}
