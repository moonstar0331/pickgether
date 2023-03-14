package com.capstone.pick.config;

import com.capstone.pick.dto.UserDto;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.security.VotePrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
