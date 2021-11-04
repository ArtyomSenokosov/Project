package ru.mail.senokosov.artem.web.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}