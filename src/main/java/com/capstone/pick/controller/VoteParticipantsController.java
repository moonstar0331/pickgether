package com.capstone.pick.controller;

import com.capstone.pick.dto.UserDto;
import com.capstone.pick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class VoteParticipantsController {
    private final UserService userService;

    @GetMapping("/{voteId}/participants")
    public String participant(@PathVariable Long voteId, Model model) {
        List<UserDto> participants = userService.getParticipants(voteId);
        model.addAttribute("participants", participants);
        return "page/participants";
    }
}
