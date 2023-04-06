package com.capstone.pick.controller;

import com.capstone.pick.controller.form.AddMoreInfoForm;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.security.VotePrincipal;
import com.capstone.pick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "page/login";
    }


    /**
     * 소셜로그인시 추가정보를 화면을 요청한다
     * @param  votePrincipal 유저정보
     * @return 추가정보 입력 화면으로 이동
     */
    @GetMapping("/addMoreInfo")
    public String addMoreInfo(@AuthenticationPrincipal VotePrincipal votePrincipal, Model model) {

        AddMoreInfoForm infoForm = AddMoreInfoForm.builder()
                .age_range(votePrincipal.getAge_range())
                .gender(votePrincipal.getGender())
                .build();

        model.addAttribute("addMoreInfo",infoForm );
        return "page/addMoreInfo";
    }

    /**
     * 소셜로그인시 추가정보를 받아 저장한다
     * @param  addMoreInfoForm 추가정보 요청 데이터 폼
     * @param  votePrincipal 유저정보
     * @return 타임라인 화면으로 이동
     */
    @PostMapping("/addMoreInfo")
    public String addMoreInfo(@ModelAttribute AddMoreInfoForm addMoreInfoForm, @AuthenticationPrincipal VotePrincipal votePrincipal) {
        userService.updateMoreInfo(votePrincipal.toDto(),addMoreInfoForm);
        return  "redirect:/timeline";

    }

}
