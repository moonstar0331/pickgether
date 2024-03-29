package com.capstone.pick.controller;

import com.capstone.pick.controller.form.SearchForm;
import com.capstone.pick.controller.form.VoteForm;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.SearchType;
import com.capstone.pick.dto.*;
import com.capstone.pick.exeption.*;
import com.capstone.pick.repository.cache.BookmarkCacheRepository;
import com.capstone.pick.repository.cache.CommentLikeCacheRepository;
import com.capstone.pick.repository.cache.PickCacheRepository;
import com.capstone.pick.repository.cache.UserCacheRepository;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class VoteController {

    private final VoteService voteService;
    private final VoteCommentService voteCommentService;
    private final UserService userService;
    private final BookmarkCacheRepository bookmarkCacheRepository;
    private final CommentLikeCacheRepository commentLikeRedisRepository;
    private final VoteResultService voteResultService;
    private final PickCacheRepository pickCacheRepository;
    private final FollowService followService;

    private final PickService pickService;

    private final UserCacheRepository userCacheRepository;

    @GetMapping("/")
    public String home() {
        return "redirect:/timeline";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String hashtag, Model map) {
        if (hashtag != null) {
            List<VoteOptionCommentDto> votes = voteService.searchVotes(SearchType.HASHTAG, hashtag);
            map.addAttribute("votes", votes);
            Set<Object> bookmarks = bookmarkCacheRepository.getAll().keySet();
            map.addAttribute("bookmarks", bookmarks);
        }
        return "page/search";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute SearchForm searchForm, ModelMap map, @AuthenticationPrincipal VotePrincipal votePrincipal) {
        if ((searchForm.getSearchType() == SearchType.USER) || (searchForm.getSearchType() == SearchType.NICKNAME)) {
            List<UserDto> users = userService.searchUsers(searchForm.getSearchType(), searchForm.getSearchValue());
            map.addAttribute("users", users);
        } else {
            List<VoteOptionCommentDto> votes = voteService.searchVotes(searchForm.getSearchType(), searchForm.getSearchValue());
            List<VoteOptionCommentDto> filteredVotes = voteService.participantsRestriction(votes, votePrincipal);
            map.addAttribute("votes", filteredVotes);
        }
        Set<Object> bookmarks = bookmarkCacheRepository.getAll().keySet();
        map.addAttribute("bookmarks", bookmarks);
        return "page/search :: #searchResult";
    }

    @GetMapping("/timeline")
    public String timeLine(@RequestParam(required = false, defaultValue = "ALL") Category category, @AuthenticationPrincipal VotePrincipal votePrincipal,
                           @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC, size = 5) Pageable pageable,
                           Model model) {

        List<VoteOptionCommentDto> votes = voteService.viewTimeLine(category, pageable)
                .stream()
                .collect(Collectors.toList());
        Set<Object> bookmarks = bookmarkCacheRepository.getAll().keySet();
        List<VoteOptionCommentDto> filteredVotes = voteService.participantsRestriction(votes, votePrincipal);

        Map<Long, PickCachingDto> picks = pickCacheRepository.getAll();

        UserDto user = userService.findUserById(votePrincipal.getUsername());

        List<FollowDto> followingList = followService.getFollowingList(user.getUserId());
        List<FollowDto> followerList = followService.getFollowerList(user.getUserId());

        model.addAttribute("user", user);
        model.addAttribute("picks", picks);
        model.addAttribute("votes", filteredVotes);
        model.addAttribute("votes", votes);
        model.addAttribute("bookmarks", bookmarks);
        model.addAttribute("category", category);
        model.addAttribute("userId", votePrincipal.getUsername());
        model.addAttribute("followingList", followingList);
        model.addAttribute("followerList", followerList);
        return "page/timeLine";
    }

    //timeline?page=size -> 기존 컨트롤러 사용하기
    @GetMapping(value = "/timeline-update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> timeLineUpdate(@RequestParam(required = false, defaultValue = "ALL") Category category,
                                              @AuthenticationPrincipal VotePrincipal votePrincipal,
                                              @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        List<VoteOptionCommentDto> votes = voteService.viewTimeLine(category, pageable)
                .stream()
                .collect(Collectors.toList());
        Set<Object> bookmarks = bookmarkCacheRepository.getAll().keySet();
        List<VoteOptionCommentDto> filteredVotes = voteService.participantsRestriction(votes, votePrincipal);

        Map<String, Object> response = new HashMap<>();
        response.put("votes", filteredVotes);
        response.put("bookmarks", bookmarks);
        response.put("category", category);

        return response;
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

    @PostMapping("/{voteId}/delete")
    public String deleteVote(
            @AuthenticationPrincipal VotePrincipal votePrincipal,
            @PathVariable Long voteId) throws VoteIsNotExistException, PermissionDeniedException {
        voteService.deleteVote(voteId, votePrincipal.getUsername());
        return "redirect:/timeline";
    }

    @GetMapping("/{voteId}/detail")
    public String voteDetail(@AuthenticationPrincipal VotePrincipal votePrincipal, @PathVariable Long voteId, @PageableDefault(sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) throws VoteIsNotExistException, JsonProcessingException {
        VoteWithOptionDto vote = voteService.getVoteWithOption(voteId);
        model.addAttribute("vote", vote);

        Page<CommentWithLikeCountDto> comments = voteCommentService.commentsByVote(voteId, pageable);
        model.addAttribute("comments", comments);

        List<Long> likes = commentLikeRedisRepository.findAll().stream().map(CommentLikeDto::getVoteCommentId).collect(Collectors.toList());
        model.addAttribute("likes", likes);

        Map<Long, PickCachingDto> picks = pickCacheRepository.getAll();
        model.addAttribute("picks", picks);

        if (votePrincipal == null) {
            model.addAttribute("isBookmark", false);
            model.addAttribute("user", null);
        } else {
            model.addAttribute("isBookmark", voteService.findBookmarkVoteId(votePrincipal.getUsername()).contains(voteId));
            model.addAttribute("user", votePrincipal.toDto());
        }
        return "page/voteDetail";
    }

    @GetMapping("/myBookmark")
    public String bookmark(@AuthenticationPrincipal VotePrincipal votePrincipal, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) throws UserNotFoundException {
        Page<VoteOptionCommentDto> votes = voteService.viewBookmarks(votePrincipal.getUsername(), pageable);
        model.addAttribute("votes", votes);
        return "page/bookmark";
    }

    @PostMapping("/{voteId}/saveBookmark")
    @ResponseBody
    public String saveBookmark(@AuthenticationPrincipal VotePrincipal votePrincipal, @PathVariable Long voteId) {
        voteService.saveBookmark(votePrincipal.getUsername(), voteId);
        return null;
    }

    @DeleteMapping("/{voteId}/deleteBookmark")
    @ResponseBody
    public String deleteBookmark(@AuthenticationPrincipal VotePrincipal votePrincipal, @PathVariable Long voteId) throws BookmarkNotFoundException, UserNotFoundException {
        voteService.deleteBookmark(votePrincipal.getUsername(), voteId);
        return null;
    }

    @DeleteMapping("/deleteAllBookmark")
    public String deleteAllBookmark(@AuthenticationPrincipal VotePrincipal votePrincipal, @PageableDefault(sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) throws UserMismatchException {
        voteService.deleteAllBookmark(votePrincipal.getUsername());
        return "redirect:";
    }

    @GetMapping("/{voteId}/analysis")
    public String voteAnalysis(@PathVariable long voteId, Model model) throws Exception {
        VoteWithOptionDto vote = voteService.getVoteWithOption(voteId);
        model.addAttribute("vote", vote);

        PickCachingDto pick = pickCacheRepository.getPick(voteId);
        model.addAttribute("pick", pick);

        Map<Long, Long> pickCountList = pickService.getPickCountList(voteId);
        model.addAttribute("pickCountList", pickCountList);

        VoteAnalysisDto analysis = VoteAnalysisDto.from(voteId, voteResultService.getVoteResults(voteId));
        model.addAttribute("analysis", analysis);

        String optionAnalysis = "";
        for (int i = 0; i < analysis.getOptionAnalysisList().size(); i++)
            optionAnalysis += ((i+1) + "번 " + analysis.getOptionAnalysisList().get(0).toString());
        model.addAttribute("optionAnalysis", optionAnalysis);
        return "page/voteAnalyze";
    }

    @GetMapping("/{voteId}/analysis/csv")
    public ResponseEntity<byte[]> voteAnalysisCSV(@PathVariable Long voteId) throws Exception {
        List<List<String>> analysis = voteResultService.getVoteResults(voteId);

        String filename = "analysis_" + voteId;
        StringWriter sw = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(sw, CSVFormat.DEFAULT);
        csvPrinter.printRecords(analysis);
        sw.flush();
        byte[] csvFile = sw.toString().getBytes("euc-kr");
        csvPrinter.close();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf("plain/text"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + ".csv");
        header.setContentLength(csvFile.length);

        return new ResponseEntity<byte[]>(csvFile, header, HttpStatus.OK);
    }
}