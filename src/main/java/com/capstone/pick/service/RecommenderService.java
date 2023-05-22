package com.capstone.pick.service;

import com.capstone.pick.dto.VoteDto;
import com.capstone.pick.recommender.VoteRecommender;
import com.capstone.pick.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommenderService {
    private final VoteRecommender contentBasedRecommender;
    private final VoteRepository voteRepository;
    public List<VoteDto> recommendVote(String userId) {
        // voteId가 반환됨
        List<Long> recommend = contentBasedRecommender.recommend(Long.valueOf(userId.hashCode()), 1);
        List<VoteDto> recommendVote = new ArrayList<>();
        recommend.stream().forEach(id -> {
            recommendVote.add(VoteDto.from(voteRepository.getReferenceById(id)));
        });
        return recommendVote;
    }
}
