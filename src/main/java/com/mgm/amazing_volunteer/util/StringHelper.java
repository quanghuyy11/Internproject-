package com.mgm.amazing_volunteer.util;

import com.mgm.amazing_volunteer.exception.EmptyFieldException;

import org.springframework.util.StringUtils;

public class StringHelper {
    public static Long getFormIdFromJotformUrl(final String url) throws EmptyFieldException {
        if(StringUtils.isEmpty(url)) throw new EmptyFieldException("Jotform url null or empty");
        final String[] urlParts = url.trim().split("/");
        final String id = urlParts[urlParts.length-1];
        return Long.parseLong(id);
    }
}
