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
     * @brief 회원가입 양식을 불러온다
     * @param  model 회원가입 폼 저장
     * @return 회원가입 뷰
     */
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signUpForm",new SignUpForm());
        return "page/signup";
    }

    /**
     * @brief 사용자 정보를 저장한다
     * @param signUpForm 신규 회원 데이터
     * @return 로그인 화면으로 이동
     */
    @PostMapping("/signup")
    public String signup(@ModelAttribute SignUpForm signUpForm) throws DuplicatedUserException {
        customUserDetailsService.save(signUpForm.toDto());
        return  "redirect:/login";
    }
}
