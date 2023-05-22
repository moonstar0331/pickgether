package com.capstone.pick.recommender;

import lombok.extern.log4j.Log4j;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;

import java.io.File;

@Log4j
public class VoteDataModel {
    private String root = "src/main/java/com/capstone/pick/recommender";
    private String path = root + "/sample_data.csv";

    public void createDataModel() {
        // 유저, 게시글, 선호도
    }

    public FileDataModel getDataModel() {
        FileDataModel fileDataModel = null;
        try {
            fileDataModel = new FileDataModel(new File(path));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return fileDataModel;
    }
}
