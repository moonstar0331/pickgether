package com.capstone.pick.controller;

import com.capstone.pick.config.CustomUserDetailsService;
import com.capstone.pick.controller.form.SignUpForm;
import com.capstone.pick.controller.request.SignupRequest;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.exeption.DuplicatedUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@Controller
public class SignupController {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * @param model 회원가입 폼 저장
     * @return 회원가입 뷰
     * @brief 회원가입 양식을 불러온다
     */
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "page/signup";
    }

//    /**
//     * @param signUpForm 신규 회원 데이터
//     * @return 로그인 화면으로 이동
//     * @brief 사용자 정보를 저장한다
//     */
    @PostMapping(value = "/signup")
    public String signup(@RequestBody SignupRequest request) throws DuplicatedUserException {
        UserDto userDto = UserDto.builder()
                .userId(request.getUserId())
                .userPassword(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .nickname(request.getNickname())
                .memo(request.getMemo())
                .gender(request.getGender())
                .birthday(request.getBirthday())
                .provider(request.getProvider())
                .job(request.getJob())
                .address(request.getAddress().split(" ")[0])
                .age_range(((LocalDate.now().getYear() - Integer.parseInt((request.getBirthday().split("-"))[0])) / 10) * 10 + "대")
                .build();
        customUserDetailsService.save(userDto);
        return "redirect:/login";
    }
}
