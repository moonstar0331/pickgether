package com.capstone.pick.handler;

import com.capstone.pick.controller.SignupController;
import com.capstone.pick.exeption.DuplicatedUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = SignupController.class)
public class SignupExceptionHandler {

    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<ErrorResponse> DuplicatedUserException() {
        ErrorResponse errorResponse = new ErrorResponse(409, "해당 ID를 샤용할 수 없습니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}

