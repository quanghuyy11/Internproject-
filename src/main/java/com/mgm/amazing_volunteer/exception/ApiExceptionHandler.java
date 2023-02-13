package com.mgm.amazing_volunteer.exception;

import com.mgm.amazing_volunteer.util.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleAllException(Exception ex, WebRequest request) {
        return new ErrorMessage(500, ex.getLocalizedMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(Exception ex, WebRequest request) {
        return new ErrorMessage(404, ex.getMessage());
    }
    @ExceptionHandler(PrizeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleCreateException(Exception ex, WebRequest request) {
        return new ErrorMessage(400, ex.getMessage());
    }
    @ExceptionHandler(PrizeRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handlePrizeResquestException(Exception ex, WebRequest request) {
        return new ErrorMessage(400, ex.getMessage());
    }

    @ExceptionHandler(RequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleRequestException(Exception ex, WebRequest request) {
        return new ErrorMessage(400, ex.getMessage());
    }
}
