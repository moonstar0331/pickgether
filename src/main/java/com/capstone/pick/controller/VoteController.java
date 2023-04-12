package com.capstone.pick.controller;

import com.capstone.pick.controller.form.SearchForm;
import com.capstone.pick.controller.form.VoteForm;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.SearchType;
import com.capstone.pick.dto.*;
import com.capstone.pick.exeption.UserMismatchException;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.UserService;
import com.capstone.pick.service.VoteCommentService;
import com.capstone.pick.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class VoteController {

    private final VoteService voteService;
    private final VoteCommentService voteCommentService;
    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "redirect:/timeline";
    }

    @GetMapping("/search")
    public String search() {
        return "page/search";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute SearchForm searchForm, ModelMap map) {
        if (searchForm.getSearchType() == SearchType.USER) {
            List<UserDto> users = userService.findUsersById(searchForm.getSearchValue());
            map.addAttribute("users", users);
        } else {
            List<VoteWithOptionDto> votes = voteService.searchVotes(searchForm.getSearchType(), searchForm.getSearchValue());
            map.addAttribute("votes", votes);
        }
        return "page/search :: #searchResult";
    }

    @GetMapping("/timeline")
    public String timeLine(@RequestParam(required = false, defaultValue = "ALL") Category category, @AuthenticationPrincipal VotePrincipal votePrincipal,
                           Model model, @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<VoteOptionCommentDto> votes = voteService.viewTimeLine(category, pageable);
        model.addAttribute("votes", votes);

        // TODO : 북마크 저장된 게시글은 어떻게 처리할지 논의
        List<Long> bookmarks = voteService.findBookmarkVoteId(votePrincipal.getUsername());
        model.addAttribute("bookmarks", bookmarks);

        model.addAttribute("category", category);
        return "page/timeLine";
    }

    @GetMapping("/form")
    public String createVote(Model model) {
        VoteForm voteForm = VoteForm.builder().build();
        model.addAttribute("voteForm", voteForm);
        return "page/form";
    }

    @PostMapping("/form")
    public String saveVote(@AuthenticationPrincipal VotePrincipal votePrincipal,
                           @ModelAttribute VoteForm voteForm) {
        VoteDto voteDto = voteForm.toDto(votePrincipal.toDto());
        List<HashtagDto> hashtagDtos = voteForm.getHashtagDtos();
        List<VoteOptionDto> voteOptionDtos = voteForm.getVoteOptions()
                .stream()
                .map(o -> o.toDto(voteDto))
                .collect(Collectors.toList());
        voteService.saveVote(voteDto, voteOptionDtos, hashtagDtos);
        return "redirect:/timeline";
    }

    @GetMapping("/{voteId}/edit")
    public String editVote(@PathVariable Long voteId, Model model) {
        VoteForm voteForm = VoteForm.builder().build();
        VoteDto voteDto = voteService.getVote(voteId);
        List<VoteOptionDto> optionDtos = voteService.getOptions(voteId);

        model.addAttribute("voteForm", voteForm);
        model.addAttribute("voteDto", voteDto);
        model.addAttribute("optionDtos", optionDtos);
        return "page/editForm";
    }

    @PostMapping("/{voteId}/edit")
    public String editVote(@AuthenticationPrincipal VotePrincipal votePrincipal,
                           @PathVariable Long voteId, VoteForm voteForm) {
        VoteDto voteDto = voteForm.toDto(votePrincipal.toDto());
        List<HashtagDto> hashtagDtos = voteForm.getHashtagDtos();
        List<VoteOptionDto> voteOptionDtos = voteForm.getVoteOptions()
                .stream()
                .map(o -> o.toDto(voteDto))
                .collect(Collectors.toList());
        voteService.updateVote(voteId, voteDto, voteOptionDtos, hashtagDtos);
        return "redirect:/timeline";
    }

    @ResponseBody
    @DeleteMapping("/deleteVote")
    public String deleteVote(
            @AuthenticationPrincipal VotePrincipal votePrincipal,
            @PathVariable Long voteId) {
        voteService.deleteVote(voteId, votePrincipal.getUsername());
        return "redirect:/timeline";
    }

    @GetMapping("/{voteId}/detail")
    public String voteDetail(@AuthenticationPrincipal VotePrincipal votePrincipal, @PathVariable Long voteId, @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        VoteWithOptionDto vote = voteService.getVoteWithOption(voteId);
        model.addAttribute("vote", vote);
        model.addAttribute("isBookmark", voteService.findBookmarkVoteId(votePrincipal.getUsername()).contains(voteId));

        Page<CommentWithLikeCountDto> comments = voteCommentService.commentsByVote(voteId, pageable);
        model.addAttribute("comments", comments);
        return "page/voteDetail";
    }

    @GetMapping("/{userId}/bookmark")
    public String bookmark(@PathVariable String userId, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<VoteOptionCommentDto> votes = voteService.viewBookmarks(userId, pageable);
        model.addAttribute("votes", votes);
        return "page/bookmark";
    }

    @PostMapping("/{voteId}/saveBookmark")
    public String saveBookmark(@AuthenticationPrincipal VotePrincipal votePrincipal, @PathVariable Long voteId) {
        voteService.saveBookmark(votePrincipal.getUsername(), voteId);
        return null;
    }

    @DeleteMapping("/{voteId}/deleteBookmark")
    public String deleteBookmark(@AuthenticationPrincipal VotePrincipal votePrincipal, @PathVariable Long voteId) throws UserMismatchException {
        voteService.deleteBookmark(votePrincipal.getUsername(), voteId);
        return null;
    }

    @DeleteMapping("/deleteAllBookmark")
    public String deleteAllBookmark(@AuthenticationPrincipal VotePrincipal votePrincipal, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) throws UserMismatchException {
        voteService.deleteAllBookmark(votePrincipal.getUsername());
        return null;
    }
}