package com.capstone.pick.controller;

import com.capstone.pick.controller.request.EditProfileRequest;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.dto.VoteOptionCommentDto;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.FollowService;
import com.capstone.pick.service.UserService;
import com.capstone.pick.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class ProfilesController {
    private final UserService userService;
    private final VoteService voteService;
    private final FollowService followService;

    @GetMapping("/profile")
    public String profiles(@AuthenticationPrincipal VotePrincipal votePrincipal, Model model) {
        UserDto user = userService.findUserById(votePrincipal.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("accountId", votePrincipal.getUsername());
        model.addAttribute("followingCnt", followService.getFollowingList(votePrincipal.getUsername()).size());
        model.addAttribute("followerCnt", followService.getFollowerList(votePrincipal.getUsername()).size());
        return "page/profile";
    }

    // 유저가 작성한 투표 게시글을 반환하는 API
    @GetMapping(value = "/get-my-vote", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findMyVote(@AuthenticationPrincipal VotePrincipal votePrincipal, @RequestParam(required = true) String userId, @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        Page<VoteOptionCommentDto> myVote = voteService.findMyVote(userId, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("myVote", myVote);
        return response;
    }

    @GetMapping("/edit-profile")
    public String editProfile(@AuthenticationPrincipal VotePrincipal votePrincipal, Model model) {
        UserDto user = userService.findUserById(votePrincipal.getUsername());
        model.addAttribute("user", user);
        return "page/editProfile";
    }

    @PostMapping("/edit-profile")
    public String editProfile(@AuthenticationPrincipal VotePrincipal votePrincipal,
                              @RequestBody EditProfileRequest request) {
        UserDto userDto = votePrincipal.toDto();
        userService.editProfile(userDto, request.getNickname(), request.getBirthday(),
                request.getGender(), request.getJob(), request.getMemo());

        return "redirect:/profile";
    }
}
