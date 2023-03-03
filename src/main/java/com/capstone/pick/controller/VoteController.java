package com.capstone.pick.controller;

import com.capstone.pick.dto.VoteDto;
import com.capstone.pick.service.VoteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VoteController {
    private static VoteService voteService;
    @GetMapping("/")
    public String postNewVote(VoteDto voteDto) {
        // 엔티티로 받아와야하는데 일단 DTO로 받아옴
        voteService.saveVote(voteDto);
        return "";
    }
}
