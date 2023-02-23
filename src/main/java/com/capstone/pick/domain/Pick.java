package com.capstone.pick.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Pick {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pick_id")
    private Long id; // 투표 선택지 id

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user; // 유저 id

    @ManyToOne(optional = false)
    @JoinColumn(name = "vote_option_id")
    private VoteOption voteOption; // 선택지 id

    protected Pick() {}

    private Pick(User user, VoteOption voteOption) {
        this.user = user;
        this.voteOption = voteOption;
    }

    public static Pick of(User user, VoteOption voteOption) {
        return new Pick(user, voteOption);
    }
}
