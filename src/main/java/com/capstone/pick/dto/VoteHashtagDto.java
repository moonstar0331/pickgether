package com.capstone.pick.dto;

import com.capstone.pick.domain.Hashtag;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.VoteHashtag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteHashtagDto {
    private Long id;
    private VoteDto voteDto;
    private HashtagDto hashtagDto;

    public static VoteHashtagDto from(VoteHashtag entity) {
        return VoteHashtagDto.builder()
                .id(entity.getId())
                .voteDto(VoteDto.from(entity.getVote()))
                .hashtagDto(HashtagDto.from(entity.getHashtag()))
                .build();
    }

    public VoteHashtag toEntity(Vote vote, Hashtag hashtag) {
        return VoteHashtag.builder()
                .vote(vote)
                .hashtag(hashtag)
                .build();
    }
}
