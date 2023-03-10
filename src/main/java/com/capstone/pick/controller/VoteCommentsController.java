package com.capstone.pick.controller;

import com.capstone.pick.controller.form.CommentForm;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.VoteCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @PostMapping("/{voteId}/comments/{commentId}/edit")
    public String updateComment(
            @AuthenticationPrincipal VotePrincipal votePrincipal,
            @PathVariable Long voteId, @PathVariable Long commentId,
            CommentForm commentForm) throws Exception {

        voteCommentService.updateComment(commentId, commentForm.toDto(votePrincipal.toDto()));
        return "redirect:/" + voteId + "/comments";
    }
}
