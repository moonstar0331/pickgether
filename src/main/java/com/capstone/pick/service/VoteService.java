package com.capstone.pick.service;

import com.capstone.pick.domain.*;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.SearchType;
import com.capstone.pick.dto.*;
import com.capstone.pick.exeption.UserMismatchException;
import com.capstone.pick.repository.*;
import com.capstone.pick.repository.cache.BookmarkCacheRepository;
import com.capstone.pick.repository.cache.VoteRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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
    private final BookmarkRepository bookmarkRepository;

    private final VoteRedisRepository voteRedisRepository;
    private final BookmarkCacheRepository bookmarkCacheRepository;

    @Transactional(readOnly = true)
    public List<VoteDto> findAllVotes() {
        List<Vote> votes = voteRepository.findAll();
        return votes.stream()
                .map(VoteDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<VoteOptionCommentDto> viewTimeLine(Category category, Pageable pageable) {
        List<VoteOptionCommentDto> votes = new ArrayList<>();

        if(category.equals(Category.ALL)) {

//            voteRedisRepository.findAll().forEach(votes::add);
//            if(!votes.isEmpty()) return new PageImpl<>(votes, pageable, votes.size());
//            else {
                Page<VoteOptionCommentDto> pages = voteRepository.findAll(pageable).map(VoteOptionCommentDto::from);
                voteRedisRepository.saveAll(pages);
                return pages;
//            }
        }
//        votes.addAll(voteRedisRepository.findAllByCategory(category));
//        if(!votes.isEmpty()) return new PageImpl<>(votes, pageable, votes.size());
//        else {
            Page<VoteOptionCommentDto> pages = voteRepository.findAllByCategory(category, pageable).map(VoteOptionCommentDto::from);
            voteRedisRepository.saveAll(pages);
            return pages;
//        }
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
//        voteRedisRepository.save(VoteOptionCommentDto.from(voteRepository.getReferenceById(savedVote.getId())));
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

    public VoteWithOptionDto getVoteWithOption(Long voteId) {
        return VoteWithOptionDto.from(voteRepository.getReferenceById(voteId));
    }

    public List<VoteOptionDto> getOptions(Long voteId) {
        return voteOptionRepository.findAllByVoteId(voteId).stream()
                .map(VoteOptionDto::from)
                .collect(Collectors.toList());
    }

    public void saveBookmark(String userId, Long voteId) {
        User user = userRepository.getReferenceById(userId);
        Vote vote = voteRepository.getReferenceById(voteId);
        bookmarkRepository.findByUserAndVote(user, vote).ifPresent(l -> {
            throw new IllegalStateException("이미 저장된 게시글입니다.");
        });
        Bookmark savedBookmark = bookmarkRepository.save(Bookmark.builder()
                .user(user)
                .vote(vote)
                .createAt(LocalDateTime.now())
                .build());
        bookmarkCacheRepository.setBookmark(BookmarkDto.from(savedBookmark));
    }

    public void deleteBookmark(String userId, Long voteId) throws UserMismatchException {
        User user = userRepository.getReferenceById(userId);
        Bookmark bookmark = bookmarkRepository.findByUserAndVoteId(user, voteId);

        if (bookmark.getUser().equals(user)) {
            bookmarkRepository.delete(bookmark);
            bookmarkCacheRepository.delete(voteId);
        } else {
            throw new UserMismatchException();
        }
    }

    public void deleteAllBookmark(String userId) {
        bookmarkRepository.deleteByUser(userRepository.getReferenceById(userId));
    }

    @Transactional(readOnly = true)
    public Page<VoteOptionCommentDto> viewBookmarks(String userId, Pageable pageble) {
        return bookmarkRepository.findAllByUser(userRepository.getReferenceById(userId), pageble).map(b -> VoteOptionCommentDto.from(b.getVote()));
    }

    @Transactional(readOnly = true)
    public List<Long> findBookmarkVoteId(String userId) {
        return bookmarkRepository.findAllByUser(userRepository.getReferenceById(userId)).stream().map(b -> b.getVote().getId()).collect(Collectors.toList());
    }

    public Long getPickCount(Long voteId) {
        return voteRepository.getReferenceById(voteId).getPickCount();
    }
}
