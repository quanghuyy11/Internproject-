package com.mgm.amazing_volunteer.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserExtendDto {
    private UserDto userDto;
    private Integer totalEvents;
    private Integer totalPoints;
}
