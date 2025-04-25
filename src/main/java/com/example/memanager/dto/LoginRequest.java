package com.example.memanager.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    private String username;
    private String password;

}