package com.mgm.amazing_volunteer.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Value("${amazing-volunteer.jotform.api-key}")
    private String apiKey;
}
