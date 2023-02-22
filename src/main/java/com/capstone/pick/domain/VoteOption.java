package com.capstone.pick.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class VoteOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_option_id")
    private Long id; // 투표 선택지 id

    @ManyToOne(optional = false)
    @JoinColumn(name = "vote_id")
    private Vote vote; // 투표 게시글 id

    @Column(length = 255)
    private String content; // 본문

    @Column(length = 255)
    private String imageLink; // 이미지 링크

}
