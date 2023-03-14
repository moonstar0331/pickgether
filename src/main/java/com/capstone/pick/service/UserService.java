package com.capstone.pick.service;

import com.capstone.pick.dto.UserDto;
import com.capstone.pick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
