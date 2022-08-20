package az.digirella.assignment.currency.service;

import az.digirella.assignment.currency.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author Ulphat
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                         .map(user -> new User(user.getUsername(),
                                               user.getPassword(),
                                               true,
                                               true,
                                               true,
                                               true,
                                               Collections.singletonList(new SimpleGrantedAuthority("BASIC"))))
                         .orElse(null);
    }
}
