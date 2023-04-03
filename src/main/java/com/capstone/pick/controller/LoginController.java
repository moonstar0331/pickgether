package com.capstone.pick.controller;

import com.capstone.pick.security.VotePrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "page/login";
    }


    @GetMapping("/addMoreInfo")
    public String addMoreInfo(@AuthenticationPrincipal VotePrincipal votePrincipal) {

        return "page/addMoreInfo";
    }


}
