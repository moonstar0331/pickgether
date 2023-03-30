package com.capstone.pick.controller;

import com.capstone.pick.controller.request.PickRequest;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.PickService;
import com.capstone.pick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class PickController {

    private final PickService pickService;
    private final UserService userService;

    @ResponseBody
    @PostMapping("/pick")
    public String pick(@AuthenticationPrincipal VotePrincipal votePrincipal, @RequestBody PickRequest request) {
        pickService.pick(votePrincipal.getUsername(), request.getOptionId());
        return "ok";
    }

    @GetMapping("/{voteId}/participants")
    public String participant(@PathVariable Long voteId, Model model) {
        int maxCnt = 6;
        List<UserDto> followingList = new ArrayList<>();
        List<UserDto> participants = userService.getParticipants(voteId);
        model.addAttribute("participants", participants);
        model.addAttribute("followingList", followingList);
        model.addAttribute("maxCnt", maxCnt);
        return "page/participants";
    }
}
