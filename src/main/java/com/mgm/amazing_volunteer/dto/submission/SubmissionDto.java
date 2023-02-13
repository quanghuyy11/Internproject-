package com.mgm.amazing_volunteer.dto.submission;

import com.mgm.amazing_volunteer.model.Event;
import com.mgm.amazing_volunteer.model.User;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class SubmissionDto {
    private Long id;
    private String email;
    private boolean isParticipated;
}
