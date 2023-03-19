package com.capstone.pick.controller;

import com.capstone.pick.config.CustomUserDetailsService;
import com.capstone.pick.controller.form.SignUpForm;
import com.capstone.pick.exeption.DuplicatedUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class SignupController {

    private final CustomUserDetailsService customUserDetailsService;

    /**
     * 회원가입 양식을 불러온다
     * @return 회원가입 뷰
     */
    @GetMapping("/signUp")
    public String signup(Model model) {
        model.addAttribute("signUpForm",new SignUpForm());
        return "/page/signUp";
    }

    /**
     * 회원가입을 진행한다
     * @param signUpForm 회원가입 요청 데이터 폼
     * @return 홈 화면으로 이동
     */
    @PostMapping("/signUp")
    public String signup(@ModelAttribute SignUpForm signUpForm) throws DuplicatedUserException {
        customUserDetailsService.save(signUpForm.toDto());
        return  "redirect:/";
    }
}
