package com.capstone.pick.controller;

import com.capstone.pick.controller.request.PickRequest;
import com.capstone.pick.dto.FollowDto;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.FollowService;
import com.capstone.pick.service.PickService;
import com.capstone.pick.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class PickController {

    private final PickService pickService;
    private final FollowService followService;
    private final VoteService voteService;

    @ResponseBody
    @PostMapping("/pick")
    public String pick(@AuthenticationPrincipal VotePrincipal votePrincipal, @RequestBody PickRequest request) {
        pickService.pick(votePrincipal.getUsername(), request.getOptionId());
        return "ok";
    }

    @GetMapping("/{voteId}/participants")
    public String participant(@AuthenticationPrincipal VotePrincipal votePrincipal,
                              Pageable pageable,
                              @PathVariable Long voteId, Model model) {

        List<String> followingUserIdList = followService.getFollowingList(votePrincipal.getUsername()).stream()
                .map(FollowDto::getToUser).map(UserDto::getUserId).collect(Collectors.toList());

        Page<UserDto> participants = pickService.getParticipants(voteId, pageable, followingUserIdList);

        model.addAttribute("participants", participants);
        model.addAttribute("followingUserIdList", followingUserIdList);
        model.addAttribute("maxCnt", 6);
        model.addAttribute("size", voteService.getPickCount(voteId));
        return "page/participants";
    }
}
