package com.capstone.pick.recommender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("추천시스템")
public class VoteRecommenderTest {
    VoteRecommender voteRecommender;
    @Test
    public void testRecommender() throws Exception {
        //given
        Long useerId = 1L;
        int size = 3;

        //when
        List<Long> recommend = voteRecommender.recommend(useerId, size);

        //then
        Assertions.assertEquals(recommend.size(), 3);
    }
}
