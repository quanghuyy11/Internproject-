package com.mgm.amazing_volunteer.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link com.mgm.amazing_volunteer.model.Request} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFromUserDto implements Serializable {

    @NotNull
    private Long prizeId;
    @NotNull
    private Integer quantity;
}