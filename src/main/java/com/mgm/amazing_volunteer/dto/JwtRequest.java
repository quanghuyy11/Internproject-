package com.mgm.amazing_volunteer.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String email;
    private String password;
}
