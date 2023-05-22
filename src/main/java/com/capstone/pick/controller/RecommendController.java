package com.capstone.pick.controller;

import com.capstone.pick.dto.VoteDto;
import com.capstone.pick.service.RecommenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping(value="/recommend")
public class RecommendController {
    RecommenderService recommenderService;
    public List<VoteDto> getRecommendVote(String userId) {
        return recommenderService.recommendVote(userId);
    }
}
