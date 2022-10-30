package org.example.sequity;

import lombok.RequiredArgsConstructor;
import org.example.domain.Role;
import org.springframework.security.core.GrantedAuthority;
@RequiredArgsConstructor
public class SecurityRole implements GrantedAuthority {

    private final Role role;

    @Override
    public String getAuthority() {
        return "ROLE_" + role.getName();
    }
}
