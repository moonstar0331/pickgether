package com.capstone.pick.handler;

import com.capstone.pick.controller.PickController;
import com.capstone.pick.controller.VoteController;
import com.capstone.pick.exeption.*;
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
        return "redirect:/login";
    }

    @ExceptionHandler(BookmarkNotFoundException.class)
    public String BookmarkNotFoundException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(404, "북마크를 찾을 수 없습니다");
        model.addAttribute("errorResponse",errorResponse);
        return "redirect:/timeLine";
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public String PermissionDeniedException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(403, "권한이 없습니다");
        model.addAttribute("errorResponse",errorResponse);
        return "redirect:/timeLine";
    }

    @ExceptionHandler(VoteIsNotExistException.class)
    public String VoteIsNotExistException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(404, "투표를 찾을 수 없습니다");
        model.addAttribute("errorResponse",errorResponse);
        return "redirect:/timeLine";
    }


}
