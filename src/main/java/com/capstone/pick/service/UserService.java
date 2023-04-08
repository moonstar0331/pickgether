package com.capstone.pick.service;

import com.capstone.pick.controller.form.AddMoreInfoForm;
import com.capstone.pick.domain.User;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> findUsersById(String userId) {
        return userRepository.findByUserIdContaining(userId).stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    // 추가정보를 소셜로그인 종류에 따라 업데이트한다
    public void updateMoreInfo(OAuth2User oAuth2User, AddMoreInfoForm form){

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String id;

        if(attributes.containsKey("response")){
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            id = "naver_"+(String) response.get("id");

        }else if(attributes.containsKey("kakao_account")){
            id = "kakao_"+String.valueOf(  (Long) attributes.get("id") );

        }else {
            id = "google_"+(String)attributes.get("sub");
        }

        Optional<User> user = userRepository.findById(id); // 유저를 찾고
        user.get().updateInfo(form.getGender(), form.getAge_range(), form.getJob(), form.getAddress()); // 추가로 받은 정보를 업데이트 하고
        userRepository.save(user.get()); // 정보를 저장

    }

    // 소셜로그인 종류에 따라 성별, 연령대를 받아와 추가정보 입력 폼에 넣어준다.
    public AddMoreInfoForm findAttribute(OAuth2User oAuth2User) {

        AddMoreInfoForm form = new AddMoreInfoForm();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        if(attributes.containsKey("response")){
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            form.setGender((String) response.get("gender"));
            form.setAge_range((String) response.get("age"));

            return form;

        }else if(attributes.containsKey("kakao_account")){
            Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");

            form.setGender((String) response.get("gender"));
            form.setAge_range((String) response.get("age_range"));

            return form;
        }

        return form;

    }






    }

