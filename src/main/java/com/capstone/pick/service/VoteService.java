package com.capstone.pick.service;

import com.capstone.pick.domain.Hashtag;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.VoteHashtag;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.SearchType;
import com.capstone.pick.dto.*;
import com.capstone.pick.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VoteService {

    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteOptionRepository voteOptionRepository;
    private final VoteHashtagRepository voteHashtagRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional(readOnly = true)
    public List<VoteDto> findAllVotes() {
        List<Vote> votes = voteRepository.findAll();
        return votes.stream()
                .map(VoteDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<VoteOptionCommentDto> viewTimeLine(Category category, Pageable pageable) {
        if(category.equals(Category.ALL)) {
            return voteRepository.findAll(pageable).map(VoteOptionCommentDto::from);
        }
        return voteRepository.findAllByCategory(category, pageable).map(VoteOptionCommentDto::from);
    }

    @Transactional(readOnly = true)
    public List<VoteWithOptionDto> searchVotes(SearchType searchType, String searchValue) {
        List<Vote> votes = new ArrayList<>();
        switch (searchType) {
            case TITLE:
                votes = voteRepository.findByTitleContaining(searchValue);
                break;
            case CONTENT:
                votes = voteRepository.findByContentContaining(searchValue);
                break;
            case NICKNAME:
                votes = voteRepository.findByUser_NicknameContaining(searchValue);
                break;
            case HASHTAG:
                votes = voteHashtagRepository.findByHashtag_ContentContaining(searchValue).stream()
                        .map(VoteHashtag::getVote).collect(Collectors.toList());
                break;
        }
        return votes.stream().map(VoteWithOptionDto::from).collect(Collectors.toList());
    }

    public void saveVote(VoteDto dto, List<VoteOptionDto> voteOptionDtos, List<HashtagDto> hashtagDtos) {
        User user = userRepository.getReferenceById(dto.getUserDto().getUserId());
        Vote savedVote = voteRepository.save(dto.toEntity(user));
        voteOptionDtos.forEach(o -> voteOptionRepository.save(o.toEntity(savedVote)));
        List<Hashtag> savedHashtags = hashtagDtos.stream()
                .map(h -> hashtagRepository.save(h.toEntity()))
                .collect(Collectors.toList());

        savedHashtags.forEach(hashtag -> {
            voteHashtagRepository.save(
                    VoteHashtag.builder()
                            .vote(savedVote)
                            .hashtag(hashtag)
                            .build());
        });
    }

    public void updateVote(Long voteId, VoteDto voteDto, List<VoteOptionDto> voteOptionDtos, List<HashtagDto> hashtagDtos) {
        try {
            Vote vote = voteRepository.getReferenceById(voteId);
            User user = userRepository.getReferenceById(voteDto.getUserDto().getUserId());

            if (vote.getUser().equals(user)) {
                if (!voteDto.getTitle().equals(vote.getTitle())) {
                    vote.changeTitle(voteDto.getTitle());
                }
                if (voteDto.getCategory() != vote.getCategory()) {
                    vote.changeCategory(voteDto.getCategory());
                }
                if (voteDto.getExpiredAt() != vote.getExpiredAt()) {
                    vote.changeExpiredAt(voteDto.getExpiredAt());
                }
                if (voteDto.isMultiPick() != vote.isMultiPick()) {
                    vote.changeIsMultiPick(voteDto.isMultiPick());
                }
                if (voteDto.getDisplayRange() != vote.getDisplayRange()) {
                    vote.changeDisplayRange(voteDto.getDisplayRange());
                }
                if (!voteDto.getContent().equals(vote.getContent())) {
                    vote.changeContent(voteDto.getContent());

                    voteHashtagRepository.findAllByVoteId(voteId).forEach(vh -> hashtagRepository.delete(vh.getHashtag()));
                    voteHashtagRepository.deleteAllByVoteId(voteId);

                    List<Hashtag> savedHashtags = hashtagDtos.stream()
                            .map(h -> hashtagRepository.save(h.toEntity()))
                            .collect(Collectors.toList());

                    savedHashtags.forEach(hashtag -> {
                        voteHashtagRepository.save(
                                VoteHashtag.builder()
                                        .vote(vote)
                                        .hashtag(hashtag)
                                        .build());
                    });
                }
                // TODO : 투표 선택지 수정 여부, 수정 가능한 범위 등 상의 후에 voteOption update 작성할 예정입니다
            }
        } catch (EntityNotFoundException e) {
            log.warn("투표 게시글 수정 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    public void deleteVote(Long voteId, String userId) {
        voteRepository.deleteByIdAndUser_UserId(voteId, userId);
    }

    public VoteDto getVote(Long voteId) {
        return VoteDto.from(voteRepository.getReferenceById(voteId));
    }

    public VoteOptionCommentDto getVoteOptionComment(Long voteId) {
        return VoteOptionCommentDto.from(voteRepository.getReferenceById(voteId));
    }

    public List<VoteOptionDto> getOptions(Long voteId) {
        return voteOptionRepository.findAllByVoteId(voteId).stream()
                .map(VoteOptionDto::from)
                .collect(Collectors.toList());
    }
}
