package com.capstone.pick.service;

import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.dto.CommentDto;
import com.capstone.pick.domain.VoteComment;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.repository.VoteCommentRepository;
import com.capstone.pick.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VoteCommentService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteCommentRepository voteCommentRepository;

    public void saveComment(CommentDto commentDto) {
        User user = userRepository.getReferenceById(commentDto.getUserDto().getUserId());
        Vote vote = voteRepository.getReferenceById(commentDto.getVoteId());
        voteCommentRepository.save(commentDto.toEntity(user, vote));
    }

    public void updateComment(Long commentId, CommentDto commentDto) throws Exception {
        VoteComment comment = voteCommentRepository.getReferenceById(commentId);
        User user = userRepository.getReferenceById(commentDto.getUserDto().getUserId());

        if(comment.getUser().equals(user)) {
            if(commentDto.getContent() != null) {
                comment.changeContent(commentDto.getContent());
            }
        } else {
            throw new Exception("해당 댓글을 작성한 유저가 아닙니다.");
        }
    }

    public void deleteComment(Long commentId, String userId) throws Exception {
        User user = userRepository.getReferenceById(userId);
        VoteComment voteComment = voteCommentRepository.getReferenceById(commentId);

        if(voteComment.getUser().equals(user)) {
            voteCommentRepository.delete(voteComment);
        } else {
            throw new Exception("해당 댓글을 작성한 유저가 아닙니다.");
        }
    }
}
