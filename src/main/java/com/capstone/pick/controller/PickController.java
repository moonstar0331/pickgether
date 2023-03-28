package com.capstone.pick.controller;

import com.capstone.pick.controller.request.PickRequest;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.PickService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class PickController {

    private final PickService pickService;

    @ResponseBody
    @PostMapping("/pick")
    public String pick(@AuthenticationPrincipal VotePrincipal votePrincipal, @RequestBody PickRequest request) {
        pickService.pick(votePrincipal.getUsername(), request.getOptionId());
        return "ok";
    }
}
