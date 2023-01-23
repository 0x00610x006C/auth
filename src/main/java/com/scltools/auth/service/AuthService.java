package com.scltools.auth.service;

import com.scltools.auth.AuthController;
import com.scltools.auth.data.User;
import com.scltools.auth.data.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

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

    public User login(String email, String password)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials");
        }

        return user;
    }
}
