package com.capstone.pick.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class VoteOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_option_id")
    private Long id; // 투표 선택지 id

    @ManyToOne(optional = false)
    @JoinColumn(name = "vote_id")
    private Vote vote; // 투표 게시글 id

    @Column(length = 255)
    private String content; // 선택지 내용

    @Column(length = 255)
    private String imageLink; // 이미지 링크
}
