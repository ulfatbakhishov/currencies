package az.digirella.assignment.currency.filter;

import az.digirella.assignment.currency.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.authenticated;

/**
 * @author Ulphat
 */
@Component
@RequiredArgsConstructor
public class TokenAuthFilter extends GenericFilterBean {

    private static final Collection<? extends GrantedAuthority> TOKEN_AUTH = Collections.singletonList(
            new SimpleGrantedAuthority("TOKEN"));
    private final TokenRepository repository;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            Optional.of(request)
                    .map(servletRequest -> (HttpServletRequest) request)
                    .map(httpServletRequest -> httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION))
                    .filter(header -> header.startsWith("Bearer "))
                    .map(header -> header.substring(7))
                    .flatMap(repository::findByValue)
                    .ifPresent(token -> {
                        SecurityContext context = SecurityContextHolder.createEmptyContext();
                        context.setAuthentication(authenticated(token.getUsername(), token.getValue(), TOKEN_AUTH));
                        SecurityContextHolder.setContext(context);
                    });
        }
        chain.doFilter(request, response);
    }

}
