package com.capstone.pick.handler;

import com.capstone.pick.controller.SignupController;
import com.capstone.pick.exeption.DuplicatedUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = SignupController.class)
@Slf4j
public class SignupExceptionHandler {

    /** 회원가입 시 기존에 동일한 아이디가 존재할 때 발생하는 예외를 처리 */
    @ExceptionHandler(DuplicatedUserException.class)
    public String DuplicatedUserException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(409, "해당 ID를 샤용할 수 없습니다.");
        model.addAttribute("errorResponse",errorResponse);
        return "page/signup";
    }

}

