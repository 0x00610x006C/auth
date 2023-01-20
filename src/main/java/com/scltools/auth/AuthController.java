package com.scltools.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class AuthController
{
    @Autowired
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/hello")
    public String hello()
    {
        return "hello!";
    }

    record RegisterRequest(
            @JsonProperty("first_name")
            String firstName,
            @JsonProperty("last_name")
            String lastName,
            String email,
            String password,
            @JsonProperty("password_confirm")
            String passwordConfirm
    ) {}

    record RegisterResponse(
            Long id,
            @JsonProperty("first_name")
            String firstName,
            @JsonProperty("last_name")
            String lastName,
            String email
    ) {}

    @PostMapping(value = "/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest)
    {
        User user = User.of(
                registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.email(),
                registerRequest.password()
        );

        return new RegisterResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

}
