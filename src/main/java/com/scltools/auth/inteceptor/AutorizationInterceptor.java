package com.scltools.auth.inteceptor;

import com.scltools.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
public class AutorizationInterceptor implements HandlerInterceptor
{
    private final AuthService authService;

    public AutorizationInterceptor(AuthService authService)
    {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String authorizationHeader = request.getHeader("Authorization");

        if (Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "no bearer token");

        request.setAttribute("user", authService.getUserFromToken(authorizationHeader.substring(7)));

        return true;
    }
}
