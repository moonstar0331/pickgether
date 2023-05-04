package com.capstone.pick.recommender;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class VoteRecommender {
    Long userId = 1L; // 유저 아이디는 Long 타입으로 입력됨

    public void recommendVote() throws IOException, TasteException {
        //1. 데이터파일 로드 : 유저아이디, 아이템아이디, 선호도
        DataModel model = new FileDataModel(new File("src/main/java/com/capstone/pick/recommender/sample_data.csv"));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);

        //2. 유저 기준 추천 모델 생성
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        //3. 추천 게시글 리스트 생성 : 유저에게 1개의 아이템을 추천
        List<RecommendedItem> recommendations = recommender.recommend(2, 3);
    }
// 결과 확인용 main
//    public static void main(String[] args) throws IOException, TasteException {
//        VoteRecommender recommender = new VoteRecommender();
//        recommender.recommendVote();
//    }
}
