package com.vaadin.starter.bakery.app.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;

/**
 * Implementation of {@link UserDetailsService} for user authentication.
 * <p>
 * This class is responsible for retrieving {@link User} entities from the database
 * based on the e-mail address provided in the login screen and converting them
 * into Spring Security {@link org.springframework.security.core.userdetails.User} objects.
 * </p>
 * <p>
 * It uses {@link UserRepository} to query user data and provides roles based on user entity.
 * </p>
 */
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs a new {@code UserDetailsServiceImpl} with the given {@link UserRepository}.
     *
     * @param userRepository the repository used to access user data
     */
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user details for authentication based on the provided username (e-mail address).
     * <p>
     * This method queries the database for a user matching the given e-mail address (case insensitive).
     * If found, it returns a Spring Security {@link org.springframework.security.core.userdetails.User}
     * object containing the user's e-mail, password hash, and role as granted authority.
     * If not found, it throws a {@link UsernameNotFoundException}.
     * </p>
     *
     * @param username the user's e-mail address used to search for the user
     * @return a {@link UserDetails} object for authentication
     * @throws UsernameNotFoundException if no user exists with the provided e-mail address
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(username);
        if (null == user) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        }
    }
}
