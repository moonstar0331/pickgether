package com.capstone.pick.service;

import com.capstone.pick.controller.form.VoteForm;
import com.capstone.pick.domain.*;
import com.capstone.pick.dto.VoteDto;
import com.capstone.pick.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteHashtagRepository voteHashtagRepository;

    private final HashtagRepository hashtagRepository;

    public void saveVote(VoteForm voteForm) {
        //TODO : 1) Spring Security 를 이용하여 로그인된 유저 객체 받아오기.
        User user = User.builder() //임시 객체 -> 추후 리펙토링
                .userId("testId")
                .userPassword("testPassword")
                .email("testEmail")
                .nickname("testNickname")
                .birthday(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .memo("")
                .build();
        userRepository.save(user);

        //주의 : Vote에 들어갈 User 객체가 DB에 존재해야 함. 그렇지 않으면 TransientPropertyValueException 발생
        Vote vote = Vote.builder()
                .user(user)
                .title(voteForm.getTitle())
                .content(voteForm.getContent())
                .category(voteForm.getCategory())
                .expiredAt(voteForm.getExpiredAt())
                .createAt(voteForm.getCreateAt())
                .modifiedAt(LocalDateTime.now())
                .isMultiPick(voteForm.getIsMultiPick())
                .displayRange(voteForm.getDisplayRange())
                .build();

        voteRepository.save(vote);

        //주의 : VoteOption에 들어갈 Vote 객체가 DB에 존재해야 함
        Long id = 1L;
        for (String content : voteForm.getVoteOption()) {
            voteOptionRepository.save(
                    VoteOption.builder()
                            .vote(vote)
                            .content(content)
                            .imageLink("testImageLink")
                            .build()
            );
        }
        //주의 : VoteHashtag에 들어갈 Vote, Hashtag 객체가 DB에 존재해야 함
        //TODO : 현재 hashtag 폼 데이터는 입력된 문자열을 통째로 가져오기 때문에 #를 기준으로 토큰화 해야 함.
        // 그러나 #를 기준으로 자르게 된다면 공백 문자도 해시태그에 포함되므로 적절하지 않음.
        for (String content : voteForm.getHashtag()) {
            Hashtag hashtag = Hashtag.builder()
                    .content(content)
                    .build();
            hashtagRepository.save(hashtag);
            voteHashtagRepository.save(
                    VoteHashtag.builder()
                            .vote(vote)
                            .hashtag(hashtag)
                            .build()
            );
        }
    }
}
