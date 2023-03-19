package com.capstone.pick.domain;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
@Entity
public class CommentLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id; // 투표 댓글 좋아요 id

    @ManyToOne(optional = false)
    @JoinColumn(name = "vote_comment_id")
    private VoteComment voteComment; // 투표 댓글 id

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user; // 유저 id
}
