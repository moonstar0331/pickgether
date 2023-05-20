package com.capstone.pick.controller;

import com.capstone.pick.controller.request.LikeRequest;
import com.capstone.pick.controller.request.PostCommentRequest;
import com.capstone.pick.dto.CommentDto;
import com.capstone.pick.dto.CommentLikeDto;
import com.capstone.pick.dto.CommentWithLikeCountDto;
import com.capstone.pick.exeption.UserMismatchException;
import com.capstone.pick.exeption.VoteIsNotExistException;
import com.capstone.pick.repository.cache.CommentLikeCacheRepository;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.VoteCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class VoteCommentsController {

    private final VoteCommentService voteCommentService;
    private final CommentLikeCacheRepository commentLikeRedisRepository;

    /**
     * 댓글을 조회한다
     *
     * @param voteId   게시글 id
     * @param pageable pageable
     * @param model    model
     * @return 댓글 목록 뷰
     */
    @GetMapping("/{voteId}/comments")
    public String comments(@AuthenticationPrincipal VotePrincipal votePrincipal, @PathVariable Long voteId,
                           @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) throws VoteIsNotExistException {
        Page<CommentWithLikeCountDto> comments = voteCommentService.commentsByVote(voteId, pageable);
        model.addAttribute("user", votePrincipal.toDto());
        model.addAttribute("comments", comments);
        return "page/comments";
    }

    /**
     * 댓글을 작성한다
     *
     * @param votePrincipal 사용자
     * @param voteId        게시글 id
     * @param request       댓글 저장 요청 데이터
     * @param model         model
     * @param pageable      pageable
     * @return 댓글 목록 뷰 댓글 리스트 fragment
     */
    @PostMapping("/{voteId}/comments")
    public String saveComment(@AuthenticationPrincipal VotePrincipal votePrincipal, @PathVariable Long voteId,
                              @RequestBody PostCommentRequest request, Model model, @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable) throws VoteIsNotExistException {
        CommentDto commentDto = CommentDto.builder()
                .voteId(voteId)
                .content(request.getContent())
                .userDto(votePrincipal.toDto())
                .build();
        voteCommentService.saveComment(commentDto);
        Page<CommentWithLikeCountDto> comments = voteCommentService.commentsByVote(voteId, pageable);
        model.addAttribute("comments", comments);

        List<Long> likes = commentLikeRedisRepository.findAll().stream().map(CommentLikeDto::getVoteCommentId).collect(Collectors.toList());
        model.addAttribute("likes", likes);

        model.addAttribute("user", votePrincipal.toDto());

        return "page/voteDetail :: #commentList";
    }

    /**
     * 댓글을 수정한다
     *
     * @param votePrincipal 사용자
     * @param model         model
     * @param pageable      pageable
     * @param voteId        게시글 id
     * @param commentId     댓글 id
     * @param request       댓글 수정 요청 데이터
     * @return 댓글 목록 뷰 댓글 리스트 fragment
     */
    @PutMapping("/{voteId}/comments/{commentId}")
    public String updateComment(@AuthenticationPrincipal VotePrincipal votePrincipal, Model model, @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable,
                                @PathVariable Long voteId, @PathVariable Long commentId, @RequestBody PostCommentRequest request) throws UserMismatchException, VoteIsNotExistException {
        CommentDto commentDto = CommentDto.builder()
                .voteId(voteId)
                .content(request.getContent())
                .userDto(votePrincipal.toDto())
                .build();
        voteCommentService.updateComment(commentId, commentDto);
        Page<CommentWithLikeCountDto> comments = voteCommentService.commentsByVote(voteId, pageable);
        model.addAttribute("comments", comments);

        List<Long> likes = commentLikeRedisRepository.findAll().stream().map(CommentLikeDto::getVoteCommentId).collect(Collectors.toList());
        model.addAttribute("likes", likes);

        model.addAttribute("user", votePrincipal.toDto());

        return "page/voteDetail :: #commentList";
    }

    /**
     * 댓글을 삭제한다
     *
     * @param votePrincipal 사용자
     * @param model         model
     * @param pageable      pageable
     * @param voteId        게시글 id
     * @param commentId     투표 댓글 id
     * @return 댓글 목록 뷰 댓글 리스트 fragment
     */
    @DeleteMapping("/{voteId}/comments/{commentId}")
    public String deleteComment(@AuthenticationPrincipal VotePrincipal votePrincipal, Model model, @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable,
                                @PathVariable Long voteId, @PathVariable Long commentId) throws UserMismatchException, VoteIsNotExistException {

        voteCommentService.deleteComment(commentId, votePrincipal.toDto().getUserId());
        Page<CommentWithLikeCountDto> comments = voteCommentService.commentsByVote(voteId, pageable);
        model.addAttribute("comments", comments);

        List<Long> likes = commentLikeRedisRepository.findAll().stream().map(CommentLikeDto::getVoteCommentId).collect(Collectors.toList());
        model.addAttribute("likes", likes);

        model.addAttribute("user", votePrincipal.toDto());

        return "page/voteDetail :: #commentList";
    }

    /**
     * 댓글 좋아요를 저장한다
     *
     * @param votePrincipal 사용자
     * @return null
     */
    @ResponseBody
    @PostMapping("/like")
    public String saveLike(@AuthenticationPrincipal VotePrincipal votePrincipal, @RequestBody LikeRequest request) {
        voteCommentService.saveCommentLike(CommentLikeDto.builder()
                .voteCommentId(request.getCommentId())
                .userDto(votePrincipal.toDto())
                .build());
        return null;
    }

    /**
     * 댓글 좋아요를 삭제한다
     *
     * @param votePrincipal 사용자
     * @return null
     */
    @ResponseBody
    @DeleteMapping("/like")
    public String deleteLike(@AuthenticationPrincipal VotePrincipal votePrincipal, @RequestBody LikeRequest request) throws UserMismatchException {
        voteCommentService.deleteCommentLike(request.getCommentId(), votePrincipal.toDto().getUserId());
        return null;
    }
}
