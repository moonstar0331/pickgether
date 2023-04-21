package com.capstone.pick.util;

import lombok.Getter;
import org.apache.hadoop.io.Text;

@Getter
public class VoteAnalysisParser {
    private String gender;
    private String age_range;
    private String address;
    private String job;

    public VoteAnalysisParser(Text text) {
        try {
            String[] colums = text.toString().split(",");
            gender = colums[2];
            age_range = colums[3];
            address = colums[4];
            job = colums[5];
        } catch (Exception e) {
            System.out.println("Error parsing a record: " + e.getMessage());
        }
    }
}
