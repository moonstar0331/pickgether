package com.capstone.pick.service;

import com.capstone.pick.controller.form.AddMoreInfoForm;
import com.capstone.pick.domain.User;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public void updateMoreInfo(UserDto userDto, AddMoreInfoForm form){

        Optional<User> user = userRepository.findById(userDto.getUserId()); // 유저를 찾고
        user.get().updateInfo(form.getGender(), form.getAge_range(), form.getJob(), form.getAddress()); // 추가로 받은 정보를 업데이트 하고
        userRepository.save(user.get()); // 정보를 저장

    }
}
