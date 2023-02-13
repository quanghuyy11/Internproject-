package com.mgm.amazing_volunteer.dto.event;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EventSubmissionDto {
    @NotNull
    private Long id;
    @NotNull
    @NotBlank
    private boolean isParticipated;
}
