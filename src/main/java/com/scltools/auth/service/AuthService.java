package com.scltools.auth.service;

import com.scltools.auth.data.User;
import com.scltools.auth.data.UserRepository;
import org.springframework.beans.factory.annotation.Value;
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

    private final String accessSecret;

    private final String refreshSecret;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${application.security.access-token-secret}")
                       String accessSecret,
                       @Value("${application.security.refresh-token-secret}")
                       String refreshSecret)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessSecret = accessSecret;
        this.refreshSecret = refreshSecret;
    }

    public User register(String firstName, String lastName, String email, String password, String passwordConfirm)
    {
        if (!Objects.equals(password, passwordConfirm))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password do not match with confirm");
        }

        return userRepository.save(User.of(firstName, lastName, email, passwordEncoder.encode(password)));
    }

    public Login login(String email, String password)
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials");
        }

        return Login.of(user.getId(), accessSecret, refreshSecret);
    }

    public User getUserFromToken(String token)
    {
        return userRepository.findById(Token.from(token, accessSecret))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not found"));
    }

    public Login refreshAccess(String refreshToken)
    {
        Long userId = Token.from(refreshToken, refreshSecret);

        return Login.of(userId, accessSecret, Token.of(refreshToken));
    }
}
