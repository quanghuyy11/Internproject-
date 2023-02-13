package com.mgm.amazing_volunteer.util.constants;

import lombok.Data;

@Data
public class DeleteResponse {

    private String message;

    public DeleteResponse(String message) {
        this.message = message;
    }
}
