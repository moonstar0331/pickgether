package com.capstone.pick.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
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
}
