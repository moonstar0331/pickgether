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

    /** 해당 댓글을 작성하지 않은 유저가 이를 삭제하거나 수정할 때 발생하는 예외를 처리 */
    @ExceptionHandler(UserMismatchException.class)
    public String UserMismatchException(Model model, UserMismatchException e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(403, "해당 댓글을 작성한 유저가 아닙니다.");
        return "redirect:/"+e.getVoteId()+"/comments";
    }

    /** 삭제된 투표에 참여하려고 할 경우에 발생하는 예외를 처리 */
    @ExceptionHandler(VoteIsNotExistException.class)
    public String VoteIsNotExistException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(404, "투표를 찾을 수 없습니다");
        model.addAttribute("errorResponse",errorResponse);
        return "redirect:/timeLine";
    }
}

