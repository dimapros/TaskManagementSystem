package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.authentication.AuthenticationRequest;
import com.example.taskmanagement.model.authentication.AuthenticationResponse;
import com.example.taskmanagement.service.impl.UserDetailServiceImpl;
import com.example.taskmanagement.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "Authentication Controller", description = "API для авторизации")
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final UserDetailServiceImpl userDetailsService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @Operation(summary = "Авторизоваться", description = "Авторизация и получение токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Авторизация прошла успешно, токен получен",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = Map.class))),
    })
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}

