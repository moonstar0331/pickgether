package com.capstone.pick.controller;

import com.capstone.pick.controller.form.CommentForm;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.VoteCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class VoteCommentsController {

    private final VoteCommentService voteCommentService;

    @GetMapping("/{voteId}/comments")
    public String readComments(@PathVariable Long voteId) {
        return "page/comments";
    }

    @PostMapping("/{voteId}/comments")
    public String saveComment(
            @AuthenticationPrincipal VotePrincipal votePrincipal,
            @PathVariable Long voteId,
            CommentForm commentForm) {

        voteCommentService.saveComment(commentForm.toDto(votePrincipal.toDto()));
        return "redirect:/" + voteId + "/comments";
    }
}
