package com.capstone.pick.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
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
