package com.mgm.amazing_volunteer.dto.prize;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class PrizeDto {

    private Long Id;
    @NotBlank
    @NotNull
    private String prize_name;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    @NotBlank
    private Integer point_archive;

    private Boolean isDeleted = false;
}