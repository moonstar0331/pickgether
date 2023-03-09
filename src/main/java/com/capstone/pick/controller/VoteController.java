package com.capstone.pick.controller;

import com.capstone.pick.controller.form.VoteForm;
import com.capstone.pick.dto.VoteDto;
import com.capstone.pick.dto.VoteOptionDto;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class VoteController {

    private final VoteService voteService;

    @GetMapping("/form")
    public String createVote(Model model) {
        //TODO : 임시로 만든 폼 페이지는 의논 후 처리해야 함
        VoteForm voteForm = VoteForm.builder().build();
        //th:object 사용을 위해 폼 객체를 넘겨줌
        model.addAttribute("voteForm", voteForm);
        return "/page/form";
    }

    @PostMapping("/form")
    public String saveVote(@AuthenticationPrincipal VotePrincipal votePrincipal,
                           @ModelAttribute VoteForm voteForm) {

        VoteDto voteDto = voteForm.toDto(votePrincipal.toDto());
        List<VoteOptionDto> voteOptionDtos = voteForm.getVoteOptions()
                .stream()
                .map(o -> o.toDto(voteDto))
                .collect(Collectors.toList());

        voteService.saveVote(voteDto, voteOptionDtos);
        return "redirect:/timeLine";
    }
}