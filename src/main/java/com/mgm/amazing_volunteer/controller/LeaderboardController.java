package com.mgm.amazing_volunteer.controller;

import com.mgm.amazing_volunteer.dto.user.LeaderboardUserDto;
import com.mgm.amazing_volunteer.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/")
    public ResponseEntity<List<LeaderboardUserDto>> getLeaderboardUsers() {
        return new ResponseEntity<>(leaderboardService.getLeaderboardUsers(), HttpStatus.OK);
    }
}
