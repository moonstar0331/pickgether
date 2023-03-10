package com.capstone.pick.controller;

import com.capstone.pick.controller.form.CommentForm;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.VoteCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * TODO : 댓글 상세 페이지는 각 투표 게시글에 연결되는 것이기 때문에
 *         추후에 게시글 구현이 완료되면 각 게시글에 연결하도록 하며,
 *         추가로 테스트 케이스도 수정한다. -> @PathVariable 로 연결;
 */

@RequiredArgsConstructor
@Controller
public class VoteCommentsController {

    private final VoteCommentService voteCommentService;

    @GetMapping("/comments")
    public String readComments() {
        return "page/comments";
    }

    @PostMapping("/comments/new")
    public String saveComment(
            @AuthenticationPrincipal VotePrincipal votePrincipal,
            CommentForm commentForm) {

        voteCommentService.saveComment(commentForm.toDto(votePrincipal.toDto()));
        return "redirect:/";
    }
}
