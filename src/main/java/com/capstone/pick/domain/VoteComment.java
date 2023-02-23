package com.capstone.pick.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class VoteComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_comment_id")
    private Long id; // 투표 댓글 id

    @ManyToOne(optional = false)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 2000)
    private String content;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    protected VoteComment() {}

    private VoteComment(Vote vote, User user, String content) {
        this.vote = vote;
        this.user = user;
        this.content = content;
    }

    public static VoteComment of(Vote vote, User user, String content) {
        return new VoteComment(vote, user, content);
    }
}
