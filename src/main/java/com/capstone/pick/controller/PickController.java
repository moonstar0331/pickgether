package com.capstone.pick.controller;

import com.capstone.pick.controller.request.PickRequest;
import com.capstone.pick.dto.FollowDto;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.exeption.DateExpiredException;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.FollowService;
import com.capstone.pick.service.PickService;
import com.capstone.pick.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class PickController {

    private final PickService pickService;
    private final FollowService followService;
    private final VoteService voteService;

    @ResponseBody
    @PostMapping("/pick")
    public String pick(@AuthenticationPrincipal VotePrincipal votePrincipal, @RequestBody PickRequest request) throws DateExpiredException {
        pickService.pick(votePrincipal.getUsername(), request.getOptionId());
        return "ok";
    }

    @GetMapping("/{voteId}/participants")
    public String participant(@AuthenticationPrincipal VotePrincipal votePrincipal,
                              @RequestParam(name = "page", defaultValue = "0") int page,
                              @PathVariable Long voteId, Model model) {

        List<String> followingUserIdList = followService.getFollowingList(votePrincipal.getUsername()).stream()
                .map(FollowDto::getToUser).map(UserDto::getUserId).collect(Collectors.toList());

        Page<UserDto> participants = pickService.getParticipants(voteId, PageRequest.of(page, 10), followingUserIdList);

        model.addAttribute("participants", participants);
        model.addAttribute("followingUserIdList", followingUserIdList);
        model.addAttribute("voteId", voteId);
        model.addAttribute("maxCnt", 6);
        model.addAttribute("size", voteService.getPickCount(voteId));
        return "page/participants";
    }

    @GetMapping(path = "/{voteId}/participants-update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> participantJson(@AuthenticationPrincipal VotePrincipal votePrincipal,
                                               @RequestParam(name = "page", defaultValue = "0") int page,
                                               @PathVariable Long voteId) {

        List<String> followingUserIdList = followService.getFollowingList(votePrincipal.getUsername())
                .stream().map(FollowDto::getToUser).map(UserDto::getUserId).collect(Collectors.toList());

        Page<UserDto> participants = pickService.getParticipants(voteId, PageRequest.of(page, 10), followingUserIdList);

        Map<String, Object> response = new HashMap<>();
        response.put("participants", participants);
        response.put("followingUserIdList", followingUserIdList);
        response.put("voteId", voteId);
        response.put("maxCnt", 6);
        response.put("size", voteService.getPickCount(voteId));

        return response;
    }

    @GetMapping(path = "/{voteId}/pick-count-list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> getPickCountList(@PathVariable Long voteId) {
        Map<Long, Long> pickCountList = pickService.getPickCountList(voteId);
        Map<String, Object> response = new HashMap<>();
        response.put("pickCountList", pickCountList);
        return response;
    }
}