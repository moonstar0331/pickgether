package com.capstone.pick.handler;

import com.capstone.pick.controller.LoginController;
import com.capstone.pick.exeption.EmptySpaceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = LoginController.class)
@Slf4j
public class LoginExceptionHandler {

    @ExceptionHandler(EmptySpaceException.class)
    public String EmptySpaceException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(406, "정보를 모두 채워주세요");
        model.addAttribute("errorResponse",errorResponse);
        return "redirect:/addMoreInfo";
    }
}
