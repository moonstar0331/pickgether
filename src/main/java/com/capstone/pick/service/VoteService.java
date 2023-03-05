package com.capstone.pick.service;

import com.capstone.pick.controller.form.VoteForm;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.dto.VoteDto;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public void saveVote(VoteForm voteForm) {
        //TODO : 1) 해시태그, 옵션 객체 생성 후 저장. 투표 본문과 매핑 필요.
        //TODO : 2) Spring Security 를 이용하여 로그인된 유저 객체 받아오기.
        User user = User.builder() //임시 객체
                .userId("testId")
                .userPassword("test123")
                .build();

        voteRepository.save(
                Vote.builder()
                        .user(user)
                        .title(voteForm.getTitle())
                        .content(voteForm.getContent())
                        .category(voteForm.getCategory())
                        .expiredAt(voteForm.getExpiredAt())
                        .createAt(voteForm.getCreateAt())
                        .modifiedAt(LocalDateTime.now())
                        .isMultiPick(voteForm.getIsMultiPick())
                        .displayRange(voteForm.getDisplayRange())
                        .build()
        );
    }
}
