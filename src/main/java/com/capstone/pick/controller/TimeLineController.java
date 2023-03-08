package com.capstone.pick.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TimeLineController {
    @GetMapping("/timeLine")
    public String timeLine() {
        return "/page/timeLine";
    }
}
