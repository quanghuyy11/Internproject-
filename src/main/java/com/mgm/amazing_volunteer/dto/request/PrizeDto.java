package com.mgm.amazing_volunteer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link com.mgm.amazing_volunteer.model.Prize} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrizeDto implements Serializable {
    private Long Id;
    private String prize_name;
    private String description;
    private Integer point_archive;
}