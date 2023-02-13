package com.mgm.amazing_volunteer.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

    private int statusCode;
    private String message;
}
