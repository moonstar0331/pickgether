package com.capstone.pick.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VoteCommentsController {

    @GetMapping("/comments")
    public String readComments() {
        return "page/comments";
    }
}
