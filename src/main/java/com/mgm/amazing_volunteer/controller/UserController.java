package com.mgm.amazing_volunteer.controller;

import com.mgm.amazing_volunteer.dto.user.UserInfoDto;
import com.mgm.amazing_volunteer.dto.user.UserPasswordDto;
import com.mgm.amazing_volunteer.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

import static com.mgm.amazing_volunteer.util.ValidUtils.getMessageBindingResult;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/avatar/{email}")
    public ResponseEntity<?> getAvatar(@PathVariable String email) {
        String avatar = userService.getImage(email);
        return new ResponseEntity<>(avatar, HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updateUser(@ModelAttribute final UserInfoDto user, final BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            String msg = getMessageBindingResult(bindingResult);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        userService.updateProfile(user);
        return new ResponseEntity<>("Update Successfully!", HttpStatus.OK);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(Principal principal, @RequestBody final UserPasswordDto userPasswordDto,
                                            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String msg = getMessageBindingResult(bindingResult);
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        String email = principal.getName();
        userService.changePassword(userPasswordDto, email);
        return new ResponseEntity<>("Change password successfully!", HttpStatus.OK);
    }
}
