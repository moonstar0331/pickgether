package com.capstone.pick.controller;

import com.capstone.pick.domain.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.capstone.pick.domain.constant.SearchType.USER;

@RequiredArgsConstructor
@Controller
public class ProfilesController {
    private final UserService userService;
    private final VoteService voteService;
    private final FollowService followService;

    @GetMapping("/profile")
    public String profiles(@AuthenticationPrincipal VotePrincipal votePrincipal, @RequestParam(required = false) String userId, Model model) {
        UserDto user = userService.findUserById(userId);
        if (!user.getUserId().isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("followingCnt", followService.getFollowingList(userId).size());
            model.addAttribute("followerCnt", followService.getFollowerList(userId).size());
            return "page/profile";
        }
        return "redirect:/timeline";
    }

    // 유저가 작성한 투표 게시글을 반환하는 API
    @GetMapping(value = "/get-my-vote", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findMyVote(@AuthenticationPrincipal VotePrincipal votePrincipal, @RequestParam(required = false) String userId, @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        Page<VoteOptionCommentDto> myVote = voteService.findMyVote(userId, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("myVote", myVote);

        return response;
    }

    @GetMapping("/edit-profile")
    public String editProfile(@AuthenticationPrincipal VotePrincipal votePrincipal) {
        return "page/editProfile";
    }
}
