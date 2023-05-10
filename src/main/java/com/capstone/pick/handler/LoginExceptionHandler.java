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

    /** 추가정보가 입력되지 않았을때 발생하는 예외를 처리 */
    @ExceptionHandler(EmptySpaceException.class)
    public String EmptySpaceException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(406, "정보를 모두 채워주세요");
        model.addAttribute("errorResponse",errorResponse);
        return "redirect:/addMoreInfo";
    }
}
