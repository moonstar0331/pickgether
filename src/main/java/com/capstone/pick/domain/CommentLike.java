package com.capstone.pick.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class CommentLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id; // 투표 댓글 좋아요 id

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_comment_id")
    private VoteComment voteComment; // 투표 댓글 id

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 유저 id
}
