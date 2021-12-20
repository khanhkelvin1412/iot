package com.hust.khanhkelvin.config;

import com.hust.khanhkelvin.domain.UserEntity;
import com.hust.khanhkelvin.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private final UserRepository userRepository;

    public CustomAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        LOGGER.debug("Authenticating {}", authentication.getName());

        String lowercaseLogin = authentication.getName().toLowerCase(Locale.ENGLISH);
        String credentials = (String) authentication.getCredentials();
        List<GrantedAuthority> authorities;

        Optional<UserEntity> optionalUserEntity = userRepository.findOneWithAuthoritiesByUsername(lowercaseLogin);

        if (!optionalUserEntity.isPresent()) {
            throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
        } else {
            UserEntity userEntity = optionalUserEntity.get();
            String salt = userEntity.getSalt();
            String passwordSalt = credentials.concat(".").concat(salt);
            String hashed = optionalUserEntity.get().getPassword();
            if (!BCrypt.checkpw(passwordSalt, hashed)) {
                throw new BadCredentialsException("Bad credential!");
            }

            //authority
            authorities = userEntity.getAuthorities()
                    .stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(lowercaseLogin, null, authorities);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
