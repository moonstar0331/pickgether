package com.capstone.pick.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class VoteHashtag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 투표 해시태그 id

    @ManyToOne(optional = false)
    @JoinColumn(name = "vote_id")
    private Vote vote; // 투표 게시글 id

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag; // 해시태그 id

    protected VoteHashtag() {}

    private VoteHashtag(Vote vote, Hashtag hashtag) {
        this.vote = vote;
        this.hashtag = hashtag;
    }

    public static VoteHashtag of(Vote vote, Hashtag hashtag) {
        return new VoteHashtag(vote, hashtag);
    }
}
