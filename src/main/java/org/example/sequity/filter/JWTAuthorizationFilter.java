package org.example.sequity.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.sequity.JpaUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.example.sequity.SecurityConstants.*;

@Component
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final JpaUserDetailsService service;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JpaUserDetailsService service) {
        super(authenticationManager);
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER);
        if (header == null || !header.startsWith(TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws IOException {
        String header = request.getHeader(HEADER);
        if (header != null){
            String token = JWT.require(Algorithm.HMAC256(SECRET_KEY.getBytes())).build().verify(
                    header.replace(TOKEN_PREFIX, "")).getSubject();
            if (token != null){
                User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
                return new UsernamePasswordAuthenticationToken(token, null,
                        service.loadUserByUsername(user.getLogin()).getAuthorities());
            }
        }
        return null;
    }
}
