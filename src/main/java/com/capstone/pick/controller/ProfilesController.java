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
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class ProfilesController {
    private final UserService userService;
    private final VoteService voteService;
    private final FollowService followService;

    @GetMapping("/profile/{userId}")
    public String profiles(@PathVariable String userId, Model model) {
        UserDto user = userService.findUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("accountId", user.getUserId());
        model.addAttribute("followingCnt", followService.getFollowingList(user.getUserId()).size());
        model.addAttribute("followerCnt", followService.getFollowerList(user.getUserId()).size());
        return "page/profile";
    }

    // 유저가 작성한 투표 게시글을 반환하는 API
    @GetMapping(value = "/get-my-vote", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> findMyVote(@AuthenticationPrincipal VotePrincipal votePrincipal, @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        Page<VoteOptionCommentDto> myVote = voteService.findMyVote(votePrincipal.getUsername(), pageable);
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
                request.getGender(), request.getJob(), request.getMemo(), request.getAddress());

        return "redirect:/profile";
    }

    @PostMapping(value = "/update-profileImg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateProfileImage(@AuthenticationPrincipal VotePrincipal votePrincipal,
                                                   @RequestParam(value = "file", required = false)MultipartFile multipartFile) {
        userService.updateProfileImage(votePrincipal.getUsername(), multipartFile);
        return "redirect:/edit-profile";
    }
}
