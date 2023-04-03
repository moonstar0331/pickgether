package com.capstone.pick.controller;

import com.capstone.pick.security.VotePrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfilesController {

    @GetMapping("/profile")
    public String profiles(@AuthenticationPrincipal VotePrincipal votePrincipal) {
        return "page/profile";
    }

    @GetMapping("/editProfile")
    public String editProfile(@AuthenticationPrincipal VotePrincipal votePrincipal) {
        return "page/editProfile";
    }
}
