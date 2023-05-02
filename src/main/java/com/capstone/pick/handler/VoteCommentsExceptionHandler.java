package com.capstone.pick.handler;

import com.capstone.pick.controller.VoteCommentsController;
import com.capstone.pick.exeption.UserMismatchException;
import com.capstone.pick.exeption.VoteIsNotExistException;
import lombok.extern.slf4j.Slf4j;
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

    @ExceptionHandler(VoteIsNotExistException.class)
    public String VoteIsNotExistException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(404, "투표를 찾을 수 없습니다");
        model.addAttribute("errorResponse",errorResponse);
        return "redirect:/timeLine";
    }
}

