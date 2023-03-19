package com.capstone.pick.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "/page/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "/page/signup";
    }
}
