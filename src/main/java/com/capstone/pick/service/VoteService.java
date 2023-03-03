package com.capstone.pick.service;

import com.capstone.pick.domain.User;
import com.capstone.pick.dto.VoteDto;
import com.capstone.pick.repository.UserRepository;
import com.capstone.pick.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public void saveVote(VoteDto dto) {
        User user = userRepository.getReferenceById(dto.getUserDto().getUserId());
        voteRepository.save(dto.toEntity(user));
    }
}
