package com.capstone.pick.controller;

import com.capstone.pick.controller.form.VoteForm;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String createVote(VoteForm voteForm) {
        //TODO : 개발 초기이므로 DTO가 아닌 Entity를 사용함. 추후 DTO로 리팩터링해야 함.
        System.out.println(voteForm.toString());
        //voteService.saveVote(voteForm);
        return "redirect:/";
    }
}
