package com.capstone.pick.handler;

import com.capstone.pick.controller.VoteCommentsController;
import com.capstone.pick.exeption.DuplicatedUserException;
import com.capstone.pick.exeption.UserMismatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = VoteCommentsController.class)
@Slf4j
public class VoteCommentsExceptionHandler {

    @ExceptionHandler(UserMismatchException.class)
    public String UserMismatchException(Model model, UserMismatchException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(403, "해당 댓글을 작성한 유저가 아닙니다.");
        return "redirect:/"+e.getVoteId()+"/comments";
    }
}

