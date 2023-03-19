package com.capstone.pick.config;

import com.capstone.pick.domain.User;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.exeption.DuplicatedUserException;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.security.VotePrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        VotePrincipal votePrincipal = userRepository
                .findById(username)
                .map(UserDto::from)
                .map(VotePrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username));

        return votePrincipal;

    }

    public void save(UserDto userDto) throws DuplicatedUserException {

        Optional<User> user = userRepository.findById(userDto.getUserId());

        if (user.isEmpty()){ // 동일한 아이디가 없다면 저장
            userRepository.save(userDto.toEntity());
        } else {  // 이미 아이디가 존재하면 예외처리
            throw new DuplicatedUserException();
        }
    }
}
