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
public class RoleDto {

    private final long id;
    private final String name;

    public static RoleDto toDto(Role role){
        return RoleDto.builder().id(role.getId()).name(role.getName()).build();
    }

    public static Role fromDto(RoleDto roleDto){
        return Role.builder().id(roleDto.getId()).name(roleDto.getName()).build();
    }
}
