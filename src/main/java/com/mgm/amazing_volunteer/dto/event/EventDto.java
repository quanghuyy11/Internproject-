package com.mgm.amazing_volunteer.dto.event;

import com.mgm.amazing_volunteer.dto.submission.SubmissionDto;
import com.mgm.amazing_volunteer.util.enums.EventStatus;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class EventDto {
    private Long Id;
    private String jotformUrl;
    @NotNull
    @Length(min = 1, message = "Title can't be empty")
    private String title;
    @NotNull
    private Integer point;
    @NotNull
    private Date eventDate;
    private int limitSubmission;
    private int countSubmission;
    private EventStatus status = EventStatus.UPCOMING;
    private boolean participated = false;
    private boolean isDeleted;
    private List<SubmissionDto> submissions = new ArrayList<>();
}
