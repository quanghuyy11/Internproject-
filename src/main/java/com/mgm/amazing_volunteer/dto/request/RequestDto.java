package com.mgm.amazing_volunteer.dto.request;

import com.mgm.amazing_volunteer.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * A DTO for the {@link com.mgm.amazing_volunteer.model.Request} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto implements Serializable {
    private Long Id;
    private Integer quantity;
    private String status;
    private UserDto user;
    private PrizeDto prize;
    private LocalDateTime createAt;
    private Boolean isDeleted = false;
}