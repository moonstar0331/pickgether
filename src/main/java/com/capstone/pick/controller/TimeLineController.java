package com.capstone.pick.controller;

import com.capstone.pick.controller.form.VoteForm;
import com.capstone.pick.domain.Vote;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TimeLineController {
    @GetMapping("/")
    public String timeLine() {
        return "/page/timeLine";
    }

    @PostMapping("/")
    public void createVote(VoteForm voteForm) {

        // 투표 폼에서 정보 받아옴 (엔티티 생성)
        Vote.builder()
                .title(voteForm.getTitle())
                .content(voteForm.getContent())
                .category(voteForm.getCategory());

        // VoteService.save()

        // return redirect:/;  // 생성 후에는 타임라인으로 리다이렉트로 복귀

    }
}
