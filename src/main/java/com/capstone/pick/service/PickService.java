package com.capstone.pick.service;

import com.capstone.pick.domain.Pick;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.VoteOption;
import com.capstone.pick.dto.PickCachingDto;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.exeption.DateExpiredException;
import com.capstone.pick.repository.PickRepository;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.repository.VoteOptionRepository;
import com.capstone.pick.repository.cache.PickCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PickService {

    private final PickRepository pickRepository;
    private final UserRepository userRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final PickCacheRepository pickCacheRepository;

    /**
     * @brief  사용자가 선택한 옵션정보를 저장한다
     * @param  userId 내 아이디
     * @param  optionId 선택한 옵션 아이디
     */
    public void pick(String userId, Long optionId) throws DateExpiredException {
        VoteOption option = voteOptionRepository.findById(optionId).orElseThrow(EntityNotFoundException::new);
        LocalDateTime expiredAt = option.getVote().getExpiredAt(); // 해당 투표의 만료일자 추출

        if (expiredAt.isBefore(LocalDateTime.now())){ // 만료일자가 지났다면 예외발생
            throw new DateExpiredException();
        }else{ // 그렇지 않다면 저장
            User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
            //VoteOption voteOption = option;
            Pick pick = pickRepository.save(Pick.builder().user(user).voteOption(option).build());
            pickCacheRepository.setPick(PickCachingDto.from(pick, option.getVote().getId()));
        }
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
        if(participants ==null){
            ArrayList<UserDto> list = new ArrayList<>();
            return new PageImpl<>(list, pageable, participants.size());
        }
        return new PageImpl<>(participants, pageable, participants.size());
    }

    public Map<Long, Long> getPickCountList(Long voteId) {
        return voteOptionRepository.findAllByVoteId(voteId).stream()
                .collect(Collectors.toMap(VoteOption::getId, vo -> pickRepository.getCountListByOptionId(vo.getId())));
    }
}
