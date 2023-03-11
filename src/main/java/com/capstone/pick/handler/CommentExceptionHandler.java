package com.capstone.pick.handler;

import com.capstone.pick.exeption.UserMismatchException;
import com.capstone.pick.handler.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommentExceptionHandler {


    @ExceptionHandler(UserMismatchException.class)
    public ResponseEntity<ErrorResponse> UserMismatchException() {
        ErrorResponse errorResponse = new ErrorResponse(403, "해당 댓글을 작성한 유저가 아닙니다.");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
