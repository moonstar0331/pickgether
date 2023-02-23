package com.capstone.pick.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VoteController {
    @GetMapping("/createVote")
    public String login() {
        return "/page/createVote";
    }
}
