package com.example.taskmanagement.model.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
    private String email;
    private String password;
}
