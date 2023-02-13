package com.mgm.amazing_volunteer.service;

import com.mgm.amazing_volunteer.dto.user.UserInfoDto;
import com.mgm.amazing_volunteer.dto.user.UserPasswordDto;

import java.io.IOException;

public interface UserService {

    String getImage(String email);

    void updateProfile(UserInfoDto userInfoDto) throws IOException;

    void changePassword(UserPasswordDto userPasswordDto, String email);
}
