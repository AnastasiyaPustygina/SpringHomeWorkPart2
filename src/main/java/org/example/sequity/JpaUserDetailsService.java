package org.example.sequity;

import lombok.RequiredArgsConstructor;
import org.example.dao.UserDao;
import org.example.domain.User;
import org.example.sequity.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserDao dao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = dao.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException(
                "User with login " + username + " was not found"));
        return new SecurityUser(user);
    }

}
