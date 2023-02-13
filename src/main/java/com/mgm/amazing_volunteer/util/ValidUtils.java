package com.mgm.amazing_volunteer.util;

import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public class ValidUtils {
    public static String getMessageBindingResult(final BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );

        String errorMsg = "";

        for (String key: errors.keySet()) {
            errorMsg += "Error at: " + key + ", reasons: " + errors.get(key) + "\n";
        }
        return errorMsg;
    }
}
