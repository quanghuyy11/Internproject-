package com.mgm.amazing_volunteer.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private String email;

    private String username;

    private MultipartFile avatar;
}
