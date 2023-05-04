package com.capstone.pick.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteOptionAnalysisDto {
    private Long optionId;
    private Map<String, Integer> genderAnalysis;
    private Map<String, Integer> ageAnalysis;
    private Map<String, Integer> addressAnalysis;
    private Map<String, Integer> jobAnalysis;

    public static VoteOptionAnalysisDto from(List<List<String>> results) {
        Map<String, Integer> address = new HashMap<>();
        Map<String, Integer> age = new HashMap<>();
        Map<String, Integer> gender = new HashMap<>();
        Map<String, Integer> job = new HashMap<>();

        for (List<String> result : results) {
            switch (result.get(1)) {
                case "Address":
                    address.put(result.get(2), Integer.valueOf(result.get(3)));
                    break;
                case "AgeRange":
                    age.put(result.get(2), Integer.valueOf(result.get(3)));
                    break;
                case "Gender":
                    gender.put(result.get(2), Integer.valueOf(result.get(3)));
                    break;
                case "Job":
                    job.put(result.get(2), Integer.valueOf(result.get(3)));
                    break;
            }
        }


        int total_add = address.values().stream().mapToInt(Integer::intValue).sum();
        address.replaceAll((k, v) -> v * 100 / total_add);
        int total_age = age.values().stream().mapToInt(Integer::intValue).sum();
        age.replaceAll((k, v) -> v * 100 / total_age);
        int total_gen = gender.values().stream().mapToInt(Integer::intValue).sum();
        gender.replaceAll((k, v) -> v * 100 / total_gen);
        int total_job = job.values().stream().mapToInt(Integer::intValue).sum();
        job.replaceAll((k, v) -> v * 100 / total_job);


        return VoteOptionAnalysisDto.builder()
                .optionId(Long.valueOf(results.get(0).get(0)))
                .addressAnalysis(address)
                .ageAnalysis(age)
                .genderAnalysis(gender)
                .jobAnalysis(job)
                .build();
    }
}
