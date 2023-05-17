package com.capstone.pick.config;

import com.capstone.pick.domain.User;
import com.capstone.pick.dto.BookmarkDto;
import com.capstone.pick.dto.CommentLikeDto;
import com.capstone.pick.dto.PickCachingDto;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.exeption.DuplicatedUserException;
import com.capstone.pick.repository.*;
import com.capstone.pick.repository.cache.BookmarkCacheRepository;
import com.capstone.pick.repository.cache.CommentLikeCacheRepository;
import com.capstone.pick.repository.cache.PickCacheRepository;
import com.capstone.pick.repository.cache.UserCacheRepository;
import com.capstone.pick.security.VotePrincipal;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserCacheRepository userCacheRepository;

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkCacheRepository bookmarkCacheRepository;

    private final CommentLikeRepository commentLikeRepository;
    private final CommentLikeCacheRepository commentLikeRedisRepository;

    private final PickRepository pickRepository;
    private final PickCacheRepository pickCacheRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDto user = null;
        try {
            user = userCacheRepository.getUser(username).orElseGet(() ->
                    userRepository.findById(username)
                            .map(UserDto::from)
                            .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username: " + username)));

            bookmarkRepository.findAllByUser(user.toEntity()).forEach(bookmark -> {
                bookmarkCacheRepository.setBookmark(BookmarkDto.from(bookmark));
            });

            List<CommentLikeDto> likes = commentLikeRepository.findAllByUser_UserId(user.getUserId()).stream().map(CommentLikeDto::from).collect(Collectors.toList());
            commentLikeRedisRepository.saveAll(likes);

            pickRepository.findAllByUser_UserId(username).forEach(pick -> {
                pickCacheRepository.setPick(PickCachingDto.from(pick, pick.getVoteOption().getVote().getId()));
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        userCacheRepository.setUser(user);
        return VotePrincipal.from(user, passwordEncoder);
    }

    /**
     * @brief  유저정보를 저장한다.
     * @param  userDto 유저정보
     * @exception DuplicatedUserException 기존에 동일한 아이디가 존재할 때 발생
     */
    @Transactional
    public void save(UserDto userDto) throws DuplicatedUserException {

        Optional<User> user = userRepository.findById(userDto.getUserId());

        if (user.isEmpty()) { // 동일한 아이디가 없다면 저장
            userRepository.save(userDto.toEntity());
        } else {  // 이미 아이디가 존재하면 예외처리
            throw new DuplicatedUserException();
        }
    }
}
