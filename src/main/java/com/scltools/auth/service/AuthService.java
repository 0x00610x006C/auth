package com.scltools.auth.service;

import com.scltools.auth.data.User;
import com.scltools.auth.data.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class AuthService
{
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String firstName, String lastName, String email, String password, String passwordConfirm)
    {
        if (!Objects.equals(password, passwordConfirm))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password do not match with confirm");
        }

        return userRepository.save(User.of(firstName, lastName, email, passwordEncoder.encode(password)));
    }
}
