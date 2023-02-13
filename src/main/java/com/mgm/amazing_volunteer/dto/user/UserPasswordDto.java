package com.mgm.amazing_volunteer.dto.user;

import lombok.Data;

@Data
public class UserPasswordDto {
    private String oldPassword;

    private String newPassword;

    private String confirmPassword;
}
