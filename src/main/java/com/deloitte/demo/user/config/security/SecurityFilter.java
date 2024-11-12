package com.deloitte.demo.user.config.security;


import com.deloitte.demo.user.config.security.exception.InvalidTokenException;
import com.deloitte.demo.user.domain.exception.UserNotFound;
import com.deloitte.demo.user.domain.model.TokenClaims;
import com.deloitte.demo.user.domain.model.UserData;
import com.deloitte.demo.user.domain.repository.UserRepository;
import com.deloitte.demo.user.domain.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {
	private static final List<String> excludedUriPatterns =
      Arrays.asList("/actuator/**", "/auth/**");
    private static final PathMatcher pathMatcher = new AntPathMatcher();

    private final UserRepository userRepository;

    private final AuthService authService;

    public SecurityFilter (UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return excludedUriPatterns.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, request.getServletPath()));
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authorizationHeader);
        String token;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        } else {
            // Handle missing or incorrect header format
            throw new InvalidTokenException("Token required");
        }

        TokenClaims claims = authService.validateToken(token);
        UserData user = userRepository.findByUsername(claims.getUsername())
            .orElseThrow(UserNotFound::new);

        var roles = claims.getRoles()
            .stream()
            .map(group -> "ROLE_" + group.name())
            .map(SimpleGrantedAuthority::new)
            .toList();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user, null, roles);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

}