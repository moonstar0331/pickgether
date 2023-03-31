package com.capstone.pick.controller;

import com.capstone.pick.controller.form.SearchForm;
import com.capstone.pick.controller.form.VoteForm;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.SearchType;
import com.capstone.pick.dto.*;
import com.capstone.pick.repository.VoteRepository;
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
        if(searchForm.getSearchType() == SearchType.USER) {
            List<UserDto> users = userService.findUsersById(searchForm.getSearchValue());
            map.addAttribute("users", users);
        } else {
            List<VoteWithOptionDto> votes = voteService.searchVotes(searchForm.getSearchType(), searchForm.getSearchValue());
            map.addAttribute("votes", votes);
        }
        return "page/search :: #searchResult";
    }

//    @GetMapping("/timeline")
//    public String timeLine(@RequestParam(required = false, defaultValue = "ALL") Category category,
//                           @RequestParam(required = false, defaultValue = "LATEST") OrderCriteria orderBy,
//                           @AuthenticationPrincipal VotePrincipal votePrincipal,
//                           Model model) {
//        List<VoteOptionCommentDto> votes = voteService.findSortedVotesByCategory(category, orderBy);
//        model.addAttribute("votes", votes);
//        model.addAttribute("category", category);
//        model.addAttribute("orderBy", orderBy);
//        model.addAttribute("userDto",votePrincipal.toDto());
//        return "page/timeLine";
//    }

    @GetMapping("/timeline")
    public String timeLine(@RequestParam(required = false, defaultValue = "ALL") Category category,
                           Model model, @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        System.out.println("category : " + category + ", " + category.getClass());
        Page<VoteOptionCommentDto> votes = voteService.viewTimeLine(category, pageable);
        model.addAttribute("votes", votes);
        model.addAttribute("category", category);
        return "page/timeLine";
    }

    @GetMapping("/form")
    public String createVote(Model model) {
        //TODO : 임시로 만든 폼 페이지는 의논 후 처리해야 함
        VoteForm voteForm = VoteForm.builder().build();
        //th:object 사용을 위해 폼 객체를 넘겨줌
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
        return "redirect:/timeline"; // 투표 게시글 상세 페이지로 돌아간다던가
    }

    @PostMapping("/{voteId}/delete")
    public String deleteVote(
            @AuthenticationPrincipal VotePrincipal votePrincipal,
            @PathVariable Long voteId) {
        voteService.deleteVote(voteId, votePrincipal.getUsername());
        return "redirect:/timeline";
    }

    @GetMapping("/{voteId}/detail")
    public String voteDetail(@PathVariable Long voteId, @RequestParam(required = false, defaultValue = "LATEST") OrderCriteria orderBy, @AuthenticationPrincipal VotePrincipal votePrincipal, Model model) {
        VoteWithOptionDto vote = voteService.getVoteWithOption(voteId);
        model.addAttribute("vote", vote);

        List<CommentPostDto> commentPosts = voteCommentService.readCommentOrderBy(voteId, orderBy).stream()
                .map(c -> CommentPostDto.builder()
                        .commentDto(c)
                        .likeCount(voteCommentService.getLikeCount(c.getId()))
                        .likeId(voteCommentService.findLikeId(c.getId(), votePrincipal.toDto().getUserId()))
                        .build())
                .collect(Collectors.toList());
        model.addAttribute("commentPosts", commentPosts);

        model.addAttribute("user", votePrincipal.toDto());
        return "page/voteDetail";
    }
}