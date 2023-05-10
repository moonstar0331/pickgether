package com.capstone.pick.service;

import com.capstone.pick.domain.*;
import com.capstone.pick.domain.constant.*;
import com.capstone.pick.dto.*;
import com.capstone.pick.exeption.*;
import com.capstone.pick.repository.*;
import com.capstone.pick.repository.cache.BookmarkCacheRepository;
import com.capstone.pick.security.VotePrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
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

    private final BookmarkCacheRepository bookmarkCacheRepository;
    private final FollowRepository followRepository;

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

    /**
     * @brief  게시글 별 제한 조건을 확인해 사용자에게 필터링 된 투표 정보를 제공
     * @param  votes 투표 게시글 목록
     * @param  votePrincipal 유저정보
     * @return 제한조건에 따라 필터링 된 게시글 목록
     */
    @Transactional(readOnly = true)
    public List<VoteOptionCommentDto> participantsRestriction(List<VoteOptionCommentDto> votes, VotePrincipal votePrincipal) {

        User user = userRepository.getReferenceById(votePrincipal.getUsername());

        String region = user.getAddress();
        String gender = user.getGender();
        String age_range = user.getAge_range();

        return votes.stream()
                .filter(Region -> Region.getUserDto().getUserId().equals(user.getUserId()) ||Region.getRegionRestriction().getDisplayValue().equals(region) || Region.getRegionRestriction().equals(RegionRestriction.All) )
                .filter(Gender -> Gender.getUserDto().getUserId().equals(user.getUserId()) ||Gender.getGenderRestriction().getDisplayValue().equals(gender) || Gender.getGenderRestriction().equals(GenderRestriction.All))
                .filter(Friends ->  Friends.getUserDto().getUserId().equals(user.getUserId()) || Friends.getDisplayRange().equals(DisplayRange.PUBLIC) || followRepository.findByFromUserAndToUser(user, Friends.getUserDto().toEntity()) !=null && followRepository.findByFromUserAndToUser(user, Friends.getUserDto().toEntity()).isFriend() )
                .filter(Age -> Age.getUserDto().getUserId().equals(user.getUserId()) || Age.getAgeRestriction().getDisplayValue().equals(age_range) || Age.getAgeRestriction().equals(AgeRestriction.All))
                .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<VoteOptionCommentDto> searchVotes(SearchType searchType, String searchValue) {
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
        return votes.stream().map(VoteOptionCommentDto::from).collect(Collectors.toList());
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
            }
        } catch (EntityNotFoundException e) {
            log.warn("투표 게시글 수정 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    /**
     * @brief 투표 게시글을 삭제한다
     * @param  voteId 게시글 정보
     * @param  userId 유저정보
     * @exception VoteIsNotExistException 투표 게시글이 이미 존재하지 않을 때 발생
     * @exception PermissionDeniedException 삭제 권한이 없을 때 발생
     */
    public void deleteVote(Long voteId, String userId) throws VoteIsNotExistException, PermissionDeniedException {
        Vote vote = voteRepository.getReferenceById(voteId);
        if (vote==null ){
            throw new VoteIsNotExistException();
        }else if(!vote.getUser().getUserId().equals(userId)){
            throw new PermissionDeniedException();

        }else{
            voteRepository.deleteByIdAndUser_UserId(voteId, userId);
        }

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

    public void deleteBookmark(String userId, Long voteId) throws BookmarkNotFoundException, UserNotFoundException {
        User user = userRepository.getReferenceById(userId);
        Bookmark bookmark = bookmarkRepository.findByUserAndVoteId(user, voteId);

        if(bookmark ==null){
            throw new BookmarkNotFoundException();
        }else{
            if (bookmark.getUser().equals(user)) {
                bookmarkRepository.delete(bookmark);
                bookmarkCacheRepository.delete(voteId);
            } else {
                throw new UserNotFoundException();
            }
        }

    }

    public void deleteAllBookmark(String userId) {
        bookmarkRepository.deleteByUser(userRepository.getReferenceById(userId));
    }

    @Transactional(readOnly = true)
    public Page<VoteOptionCommentDto> viewBookmarks(String userId, Pageable pageble) throws UserNotFoundException {
        if(userRepository.getReferenceById(userId)!=null){
            return bookmarkRepository.findAllByUser(userRepository.getReferenceById(userId), pageble).map(b -> VoteOptionCommentDto.from(b.getVote()));
        }else{
            throw new UserNotFoundException();
        }

    }

    @Transactional(readOnly = true)
    public List<Long> findBookmarkVoteId(String userId) {
        return bookmarkRepository.findAllByUser(userRepository.getReferenceById(userId)).stream().map(b -> b.getVote().getId()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<VoteOptionCommentDto> findMyVote(String userId, Pageable pageable) {
        return voteRepository.findAllByUser_UserId(userId, pageable).map(VoteOptionCommentDto::from);
    }
    public Long getPickCount(Long voteId) {
        return voteRepository.getReferenceById(voteId).getPickCount();
    }
}
