package com.capstone.pick.recommender;

import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.hibernate.id.enhanced.Optimizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class VoteRecommender {
    VoteDataModel voteDataModel = new VoteDataModel();
    public List<Long> recommend(Long userId, int size) {
        List<Long> voteIdList = new ArrayList<>();
        try {
            DataModel dataModel = voteDataModel.getDataModel();
            ItemSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            Recommender recommender = new GenericItemBasedRecommender(dataModel, similarity);
            List<RecommendedItem> recommendedItems = recommender.recommend(userId, size);
            voteIdList = recommendedItems.parallelStream()
                    .map(RecommendedItem::getItemID).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return voteIdList;
    }

    public static void main(String[] args) {
        VoteRecommender vr = new VoteRecommender();
        vr.recommend(1L, 1);
    }
}
