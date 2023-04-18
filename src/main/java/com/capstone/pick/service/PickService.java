package com.capstone.pick.service;

import com.capstone.pick.domain.Pick;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.VoteOption;
import com.capstone.pick.dto.FollowDto;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.repository.PickRepository;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.repository.VoteOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PickService {

    private final PickRepository pickRepository;
    private final UserRepository userRepository;
    private final VoteOptionRepository voteOptionRepository;

    public void pick(String userId, Long optionId) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        VoteOption voteOption = voteOptionRepository.findById(optionId).orElseThrow(EntityNotFoundException::new);
        pickRepository.save(Pick.builder().user(user).voteOption(voteOption).build());
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getParticipants(Long voteId, Pageable pageable, List<String> followingUserIdList) {
        List<UserDto> participants = pickRepository.findAllByVoteId(voteId, pageable).stream()
                .sorted((u1, u2) -> {
                    // 참여유저가 팔로잉 유저와 일치하면 true
                    boolean u1Followed = followingUserIdList.stream()
                            .anyMatch(f -> f.equals(u1.getUserId()));
                    boolean u2Followed = followingUserIdList.stream()
                            .anyMatch(f -> f.equals(u2.getUserId()));
                    // true 인 객체를 앞으로 정렬
                    return Boolean.compare(!u1Followed, !u2Followed);
                })
                .map(UserDto::from)
                .collect(Collectors.toList());
        return new PageImpl<>(participants, pageable, participants.size());
    }

    public Map<Long, Long> getPickCountList(Long voteId) {
        return voteOptionRepository.findAllByVoteId(voteId).stream()
                .collect(Collectors.toMap(VoteOption::getId, vo -> pickRepository.getCountListByOptionId(vo.getId())));
    }
}
