package com.capstone.pick.service;

import com.capstone.pick.controller.form.AddMoreInfoForm;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.constant.SearchType;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.exeption.EmptySpaceException;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.repository.cache.UserCacheRepository;
import com.capstone.pick.utils.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;
    private final AwsS3Uploader awsS3Uploader;

    public UserDto findUserById(String userId) {
        return userRepository.findById(userId).map(UserDto::from).orElseThrow(() ->
                new UsernameNotFoundException(String.format("%s not founded", userId)));
    }

    public List<UserDto> searchUsers(SearchType searchType, String searchValue) {
        List<User> users = new ArrayList<>();
        switch (searchType) {
            case USER:
                users = userRepository.findByUserIdContaining(searchValue);
                break;
            case NICKNAME:
                users = userRepository.findByNicknameContaining(searchValue);
                break;
        }
        return users.stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }


    /**
     * @brief  소셜로그인시 추가정보를 업데이트한다
     * @param  oAuth2User 소셜로그인 유저정보
     * @param  form 추가 정보
     */
    public void updateMoreInfo(OAuth2User oAuth2User, AddMoreInfoForm form) throws EmptySpaceException {

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String id;

        // 로그인 플랫폼 별로 고유 아이디를 추출한다
        if (attributes.containsKey("response")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            id = "naver_" + (String) response.get("id");

        } else if (attributes.containsKey("kakao_account")) {
            id = "kakao_" + String.valueOf((Long) attributes.get("id"));

        } else {
            id = "google_" + (String) attributes.get("sub");
        }

        if(form.getGender()==null || form.getBirthday()==null ||form.getJob()==null ||form.getAddress()==null ){
            throw new EmptySpaceException();
        }

        String[] split = form.getAddress().split(" ");
        String region = split[0]; // 주소에서 지역 정보만 추출

        Optional<User> user = userRepository.findById(id); // 유저를 찾고

        String age_range = ((LocalDate.now().getYear() - Integer.parseInt((form.getBirthday().split("-"))[0])) / 10) * 10 + "대";

        user.get().updateInfo(form.getGender(), form.getBirthday(), age_range, form.getJob(), region); // 추가로 받은 정보를 업데이트 하고
        userRepository.save(user.get()); // 정보를 저장

    }

    /**
     * @brief  소셜로그인 종류에 따라 성별, 연령대를 받아와 추가정보 입력 폼에 넣어준다.
     * @param  oAuth2User 소셜로그인 유저정보
     * @return 사용자의 일부 정보 데이터
     */
    public AddMoreInfoForm findAttribute(OAuth2User oAuth2User) {

        AddMoreInfoForm form = new AddMoreInfoForm();

        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (attributes.containsKey("response")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");

            form.setGender((String) response.get("gender"));
            form.setAge_range((String) response.get("age"));

            return form;

        } else if (attributes.containsKey("kakao_account")) {
            Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");

            form.setGender((String) response.get("gender"));
            form.setAge_range((String) response.get("age_range"));

            return form;
        }

        return form;

    }

    @Transactional
    public void editProfile(UserDto userDto, String nickname, String birthday, String gender,
                            String job, String memo, String address) {
        userDto.updateInfo(nickname, birthday, gender, job, memo, address);
        userRepository.save(userDto.toEntity());
    }

    @Transactional
    public void updateProfileImage(String userId, MultipartFile profileImg) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));

        if(!profileImg.isEmpty()) {
            if(user.getImageUrl() != null) awsS3Uploader.deleteImage(user.getImageUrl());
            String uploadImageUrl = awsS3Uploader.uploadImage(userId, profileImg);
            user.updateImageUrl(uploadImageUrl);

            userCacheRepository.update(UserDto.from(user));
        }
    }
}

