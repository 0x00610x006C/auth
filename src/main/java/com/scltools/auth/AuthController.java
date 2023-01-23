package com.scltools.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scltools.auth.data.User;
import com.scltools.auth.service.AuthService;
import com.scltools.auth.service.Login;
import com.scltools.auth.service.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api")
public class AuthController
{
    private final AuthService authService;

    public AuthController(AuthService authService)
    {
        this.authService = authService;
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
        User user = authService.register(
                registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.email(),
                registerRequest.password(),
                registerRequest.passwordConfirm()
        );

        return new RegisterResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    record LoginRequest(
            String email,
            String password
    ) {}

    record LoginResponse(
            String token
    ) {}

    @PostMapping(value = "/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletResponse response)
    {
        Login login = authService.login(loginRequest.email(), loginRequest.password());

        Cookie cookie = new Cookie("refresh_token", login.getRefreshToken().getToken());
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");

        response.addCookie(cookie);

        return new LoginResponse(login.getAccessToken().getToken());
    }

}
