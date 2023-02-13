package com.mgm.amazing_volunteer.service;

import com.mgm.amazing_volunteer.dto.user.LeaderboardUserDto;
import com.mgm.amazing_volunteer.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {

    private final UserRepository userRepository;

    @Autowired
    public LeaderboardServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<LeaderboardUserDto> getLeaderboardUsers() {
        return userRepository.getLeaderboardUsers();
    }
}
