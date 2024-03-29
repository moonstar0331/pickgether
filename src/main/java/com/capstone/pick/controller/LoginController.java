package com.capstone.pick.controller;

import com.capstone.pick.controller.form.AddMoreInfoForm;
import com.capstone.pick.exeption.EmptySpaceException;
import com.capstone.pick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
     * @brief  소셜로그인시 추가정보 화면을 요청한다
     * @param  oAuth2User 소셜로그인 유저정보
     * @param  model 유저정보의 일부 데이터 저장
     * @return 추가정보 입력 화면으로 이동
     */
    @GetMapping("/addMoreInfo")
    public String addMoreInfo(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {

        AddMoreInfoForm infoForm = userService.findAttribute(oAuth2User);
        model.addAttribute("addMoreInfo",infoForm );
        return "page/addMoreInfo";
    }

    /**
     * @brief 소셜로그인시 추가정보를 받아 저장한다
     * @param  addMoreInfoForm 추가정보 데이터
     * @param  oAuth2User 소셜로그인 유저정보
     * @return 타임라인 화면으로 이동
     */
    @PostMapping("/addMoreInfo")
    public String saveMoreInfo(@ModelAttribute AddMoreInfoForm addMoreInfoForm, @AuthenticationPrincipal OAuth2User oAuth2User) throws EmptySpaceException {
        userService.updateMoreInfo(oAuth2User,addMoreInfoForm);
        return  "redirect:/timeline";

    }

}
