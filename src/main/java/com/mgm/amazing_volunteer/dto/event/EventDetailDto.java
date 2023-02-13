package com.mgm.amazing_volunteer.dto.event;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EventDetailDto {
    @NotNull
    private Long event_id;
    @NotNull
    private List<EventSubmissionDto> submissions;
}
