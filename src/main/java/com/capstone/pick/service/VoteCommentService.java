package com.capstone.pick.service;

import com.capstone.pick.domain.CommentLike;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.VoteComment;
import com.capstone.pick.dto.CommentDto;
import com.capstone.pick.dto.CommentLikeDto;
import com.capstone.pick.dto.CommentWithLikeCountDto;
import com.capstone.pick.exeption.UserMismatchException;
import com.capstone.pick.exeption.VoteIsNotExistException;
import com.capstone.pick.repository.*;
import com.capstone.pick.repository.cache.CommentLikeCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteCommentService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteCommentRepository voteCommentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentLikeCacheRepository commentLikeRedisRepository;

    public Page<CommentWithLikeCountDto> commentsByVote(Long voteId, Pageable pageable) throws VoteIsNotExistException {
        Vote vote = voteRepository.findById(voteId).orElseThrow(()->new VoteIsNotExistException());

        return voteCommentRepository.findAllByVote(vote, pageable).map(CommentWithLikeCountDto::from);
    }

    public void saveComment(CommentDto commentDto) throws VoteIsNotExistException {
        User user = userRepository.getReferenceById(commentDto.getUserDto().getUserId());
        Vote vote = voteRepository.getReferenceById(commentDto.getVoteId());
        if(vote == null){
            throw new VoteIsNotExistException();
        }
        voteCommentRepository.save(commentDto.toEntity(user, vote));
    }

    public void updateComment(Long commentId, CommentDto commentDto) throws UserMismatchException, VoteIsNotExistException {
        Vote vote = voteRepository.getReferenceById(commentDto.getVoteId());
        if(vote == null){
            throw new VoteIsNotExistException();
        }
        VoteComment comment = voteCommentRepository.getReferenceById(commentId);
        User user = userRepository.getReferenceById(commentDto.getUserDto().getUserId());


        if (comment.getUser().equals(user)) {
            if (commentDto.getContent() != null) {
                comment.changeContent(commentDto.getContent());
            }
        } else {
            throw new UserMismatchException(comment.getVote().getId());
        }
    }

    public void deleteComment(Long commentId, String userId) throws UserMismatchException, VoteIsNotExistException {

        User user = userRepository.getReferenceById(userId);
        VoteComment voteComment = voteCommentRepository.getReferenceById(commentId);

        if(voteComment.getVote()== null){
            throw new VoteIsNotExistException();
        }

        if (voteComment.getUser().equals(user)) {
            commentLikeRepository.deleteAllByVoteCommentId(voteComment.getId());
            voteCommentRepository.delete(voteComment);
        } else {
            throw new UserMismatchException(voteComment.getVote().getId());
        }
    }

    public void saveCommentLike(CommentLikeDto commentLikeDto) {
        User user = userRepository.getReferenceById(commentLikeDto.getUserDto().getUserId());
        VoteComment voteComment = voteCommentRepository.getReferenceById(commentLikeDto.getVoteCommentId());

        commentLikeRepository.findByUserAndVoteComment(user, voteComment).ifPresent(l -> {
            throw new IllegalStateException("이미 좋아요를 했습니다.");
        });

        CommentLike savedLike = commentLikeRepository.save(commentLikeDto.toEntity(user, voteComment));
        commentLikeRedisRepository.save(CommentLikeDto.from(savedLike));
    }

    public void deleteCommentLike(Long commentId, String userId) throws UserMismatchException {
        User user = userRepository.getReferenceById(userId);
        VoteComment voteComment = voteCommentRepository.getReferenceById(commentId);
        CommentLike commentLike = commentLikeRepository.findByUserAndVoteComment(user, voteComment).orElseThrow(
                () -> new IllegalStateException("좋아요를 하지 않았습니다."));

        if(commentLike.getUser().getUserId().equals(userId)){
            commentLikeRepository.delete(commentLike);
            commentLikeRedisRepository.delete(CommentLikeDto.from(commentLike));
        }else{
            throw new UserMismatchException(voteComment.getVote().getId());
        }


    }

    public Long getLikeCount(Long commentId) {
        return commentLikeRepository.countByVoteCommentId(commentId);
    }

    public Long findLikeId(Long commentId, String userId) {
        CommentLike like = commentLikeRepository.findByVoteCommentIdAndUserUserId(commentId, userId);
        return like == null ? -1 : like.getId();
    }
}
