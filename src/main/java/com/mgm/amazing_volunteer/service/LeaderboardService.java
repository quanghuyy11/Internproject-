package com.mgm.amazing_volunteer.service;

import com.mgm.amazing_volunteer.dto.user.LeaderboardUserDto;

import java.util.List;

public interface LeaderboardService {

    List<LeaderboardUserDto> getLeaderboardUsers();
}
