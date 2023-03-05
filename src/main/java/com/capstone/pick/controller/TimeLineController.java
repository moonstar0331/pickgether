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
}
