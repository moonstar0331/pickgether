package com.capstone.pick.service;

import com.capstone.pick.domain.Pick;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.VoteOption;
import com.capstone.pick.repository.PickRepository;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.repository.VoteOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

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
}
