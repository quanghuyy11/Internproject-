package com.mgm.amazing_volunteer.service;

import com.mgm.amazing_volunteer.dto.user.UserInfoDto;
import com.mgm.amazing_volunteer.dto.user.UserPasswordDto;
import com.mgm.amazing_volunteer.exception.ComparisonException;
import com.mgm.amazing_volunteer.exception.EmptyFieldException;
import com.mgm.amazing_volunteer.exception.InvalidOldPasswordException;
import com.mgm.amazing_volunteer.exception.NotFoundException;
import com.mgm.amazing_volunteer.repository.UserRepository;
import com.mgm.amazing_volunteer.util.FileUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FileUtils fileUtils;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, FileUtils fileUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.fileUtils = fileUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String getImage(final String email) {
        if (!StringUtils.hasText(email)) {
            throw new EmptyFieldException("Email not found!");
        }
        return userRepository.getAvatarByEmail(email);
    }

    @Override
    @Transactional
    public void updateProfile(final UserInfoDto userInfoDto) {
        String fileName = fileUtils.uploadImage(userInfoDto.getAvatar());
        userRepository.findById(userInfoDto.getEmail())
                .map(user -> {
                    user.setAvatar(fileName);
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new NotFoundException("User not found!"));
    }

    @Override
    @Transactional
    public void changePassword(final UserPasswordDto userPasswordDto, final String email) {
        String oldPassword = userPasswordDto.getOldPassword();
        String dbPassword = userRepository.getPasswordByEmail(email);

        if (passwordEncoder.matches(oldPassword, dbPassword)) {
            userRepository.findById(email)
                    .map(user -> {
                        if (userPasswordDto.getNewPassword().equals(userPasswordDto.getConfirmPassword())) {
                            user.setPassword(passwordEncoder.encode(userPasswordDto.getNewPassword()));
                            return userRepository.save(user);
                        } else {
                            throw new ComparisonException("Confirm password doesn't match new password!");
                        }
                    })
                    .orElseThrow(() -> new NotFoundException("User not found!"));
        } else {
            throw new InvalidOldPasswordException("Wrong old password!");
        }
    }
}
