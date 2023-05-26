package com.capstone.pick.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteAnalysisDto {
    private Long voteId;
    private Map<String, Integer> genderAnalysis;
    private Map<String, Integer> ageAnalysis;
    private Map<String, Integer> addressAnalysis;
    private Map<String, Integer> jobAnalysis;

    public static VoteAnalysisDto from(Long voteId, List<List<String>> results) {
        Map<String, Integer> address = new HashMap<>();
        Map<String, Integer> age = new HashMap<>();
        Map<String, Integer> gender = new HashMap<>();
        Map<String, Integer> job = new HashMap<>();

        for (List<String> result : results) {
            switch (result.get(1)) {
                case "Address":
                    if (address.containsKey(result.get(2)))
                        address.replace(result.get(2), Integer.valueOf(result.get(3)) + address.get(result.get(2)));
                    else
                        address.put(result.get(2), Integer.valueOf(result.get(3)));
                    break;
                case "AgeRange":
                    if (age.containsKey(result.get(2)))
                        age.replace(result.get(2), Integer.valueOf(result.get(3)) + age.get(result.get(2)));
                    else
                        age.put(result.get(2), Integer.valueOf(result.get(3)));
                    break;
                case "Gender":
                    if (gender.containsKey(result.get(2)))
                        gender.replace(result.get(2), Integer.valueOf(result.get(3)) + gender.get(result.get(2)));
                    else
                        gender.put(result.get(2), Integer.valueOf(result.get(3)));
                    break;
                case "Job":
                    if (job.containsKey(result.get(2)))
                        job.replace(result.get(2), Integer.valueOf(result.get(3)) + job.get(result.get(2)));
                    else
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

        return VoteAnalysisDto.builder()
                .voteId(voteId)
                .addressAnalysis(address)
                .ageAnalysis(age)
                .genderAnalysis(gender)
                .jobAnalysis(job)
                .build();
    }
}
