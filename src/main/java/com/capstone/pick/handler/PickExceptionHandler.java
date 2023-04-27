package com.capstone.pick.handler;

import com.capstone.pick.controller.PickController;
import com.capstone.pick.exeption.DateExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = PickController.class)
@Slf4j
public class PickExceptionHandler {

    @ExceptionHandler(DateExpiredException.class)
    public String DateExpiredException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(406, "만료된 투표입니다.");
        model.addAttribute("errorResponse",errorResponse);
        return "redirect:/timeLine";
    }
}
