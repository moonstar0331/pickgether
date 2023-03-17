package com.capstone.pick.service;

import com.capstone.pick.domain.CommentLike;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.VoteComment;
import com.capstone.pick.dto.CommentDto;
import com.capstone.pick.dto.CommentLikeDto;
import com.capstone.pick.exeption.UserMismatchException;
import com.capstone.pick.repository.CommentLikeRepository;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.repository.VoteCommentRepository;
import com.capstone.pick.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteCommentService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteCommentRepository voteCommentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public List<CommentDto> readComment(Long voteId) {
        List<VoteComment> voteComments = voteCommentRepository.getVoteCommentsByVoteId(voteId);
        return voteComments.stream()
                .map(CommentDto::from)
                .collect(Collectors.toList());
    }

    public void saveComment(CommentDto commentDto) {
        User user = userRepository.getReferenceById(commentDto.getUserDto().getUserId());
        Vote vote = voteRepository.getReferenceById(commentDto.getVoteId());
        voteCommentRepository.save(commentDto.toEntity(user, vote));
    }

    public void updateComment(Long commentId, CommentDto commentDto) throws UserMismatchException {
        VoteComment comment = voteCommentRepository.getReferenceById(commentId);
        User user = userRepository.getReferenceById(commentDto.getUserDto().getUserId());

        if (comment.getUser().equals(user)) {
            if (commentDto.getContent() != null) {
                comment.changeContent(commentDto.getContent());
            }
        } else {
            throw new UserMismatchException();
        }
    }

    public void deleteComment(Long commentId, String userId) throws UserMismatchException {
        User user = userRepository.getReferenceById(userId);
        VoteComment voteComment = voteCommentRepository.getReferenceById(commentId);

        if (voteComment.getUser().equals(user)) {
            voteCommentRepository.delete(voteComment);
        } else {
            throw new UserMismatchException();
        }
    }

    public void saveCommentLike(CommentLikeDto commentLikeDto) {
        User user = userRepository.getReferenceById(commentLikeDto.getUserDto().getUserId());
        VoteComment voteComment = voteCommentRepository.getReferenceById(commentLikeDto.getVoteCommentId());
        if (findLikeId(voteComment.getId(), user.getUserId()) == -1) {
            commentLikeRepository.save(commentLikeDto.toEntity(user, voteComment));
        }
    }

    public void deleteCommentLike(Long commentLikeId, String userId) {
        User user = userRepository.getReferenceById(userId);
        CommentLike commentLike = commentLikeRepository.getReferenceById(commentLikeId);

        if (commentLike.getUser().equals(user)) {
            commentLikeRepository.delete(commentLike);
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
