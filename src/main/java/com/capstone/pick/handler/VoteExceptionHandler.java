package com.capstone.pick.handler;

import com.capstone.pick.controller.PickController;
import com.capstone.pick.controller.VoteController;
import com.capstone.pick.exeption.BookmarkNotFoundException;
import com.capstone.pick.exeption.DuplicatedUserException;
import com.capstone.pick.exeption.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = VoteController.class)
@Slf4j
public class VoteExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public String UserNotFoundException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(404, "없는 사용자 입니다");
        model.addAttribute("errorResponse",errorResponse);
        return "page/login";
    }

    @ExceptionHandler(BookmarkNotFoundException.class)
    public String BookmarkNotFoundException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(404, "북마크를 찾을 수 없습니다");
        model.addAttribute("errorResponse",errorResponse);
        return "page/timeLine";
    }



}
