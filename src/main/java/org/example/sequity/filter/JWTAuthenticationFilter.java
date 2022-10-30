package org.example.sequity.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.User;
import org.example.sequity.JpaUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static org.example.sequity.SecurityConstants.SECRET_KEY;

@Component
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager manager;
    private final JpaUserDetailsService service;

    public JWTAuthenticationFilter(AuthenticationManager manager, JpaUserDetailsService service) {
        super(manager);
        this.manager = manager;
        this.service = service;
        setFilterProcessesUrl("/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return manager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(),
                    user.getPassword(), service.loadUserByUsername(user.getLogin()).getAuthorities()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException,
            ServletException {
        String token = JWT.create().withSubject(((User) authResult.getPrincipal()).getLogin())
                .withExpiresAt(new Date(System.currentTimeMillis() + 15 * 60 * 1000)).sign(Algorithm.HMAC256(
                        SECRET_KEY.getBytes()));
        String resultToken = ((User) authResult.getPrincipal()).getLogin() + " " + token;
        response.getWriter().write(resultToken);
        response.getWriter().flush();
    }
}
