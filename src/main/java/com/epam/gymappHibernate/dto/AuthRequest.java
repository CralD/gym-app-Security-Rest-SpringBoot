package com.epam.gymappHibernate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String username;
    private String password;

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
