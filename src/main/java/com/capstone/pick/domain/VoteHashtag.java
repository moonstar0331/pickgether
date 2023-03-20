package com.capstone.pick.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class VoteHashtag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 투표 해시태그 id

    @ManyToOne(optional = false)
    @JoinColumn(name = "vote_id")
    private Vote vote; // 투표 게시글 id

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag; // 해시태그 내용
}
