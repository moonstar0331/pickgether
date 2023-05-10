package com.capstone.pick.handler;

import com.capstone.pick.controller.VoteController;
import com.capstone.pick.exeption.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = VoteController.class)
@Slf4j
public class VoteExceptionHandler {

    /** 사용자가 없을 때 발생하는 예외를 처리 */
    @ExceptionHandler(UserNotFoundException.class)
    public String UserNotFoundException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(404, "없는 사용자 입니다");
        model.addAttribute("errorResponse",errorResponse);
        return "redirect:/login";
    }

    /** 존재하지 않는 북마크를 삭제하려고 할 때 발생하는 예외를 처리 */
    @ExceptionHandler(BookmarkNotFoundException.class)
    public String BookmarkNotFoundException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(404, "북마크를 찾을 수 없습니다");
        model.addAttribute("errorResponse",errorResponse);
        return "redirect:/timeLine";
    }

    /** 투표 게시글에 대한 수정 권한이 없는 유저가 이를 수정하려고 할 때 발생하는 예외를 처리 */
    @ExceptionHandler(PermissionDeniedException.class)
    public String PermissionDeniedException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(403, "권한이 없습니다");
        model.addAttribute("errorResponse",errorResponse);
        return "redirect:/timeLine";
    }

    /** 투표 게시글이 이미 존재하지 않을 경우 발생하는 예외를 처리 */
    @ExceptionHandler(VoteIsNotExistException.class)
    public String VoteIsNotExistException(Model model, Exception e) {
        log.error(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(404, "투표를 찾을 수 없습니다");
        model.addAttribute("errorResponse",errorResponse);
        return "redirect:/timeLine";
    }


}
