package com.capstone.pick.service;

import com.capstone.pick.controller.form.VoteForm;
import com.capstone.pick.controller.form.VoteOptionFormDto;
import com.capstone.pick.domain.User;
import com.capstone.pick.domain.Vote;
import com.capstone.pick.domain.constant.Category;
import com.capstone.pick.domain.constant.DisplayRange;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.dto.VoteDto;
import com.capstone.pick.dto.VoteOptionDto;
import com.capstone.pick.repository.VoteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("비즈니스 서비스 로직 - 투표 게시글")
@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {

    @InjectMocks private VoteService voteService;

    @Mock private VoteRepository voteRepository;

    @DisplayName("타임라인을 조회하면, 모든 투표 게시글을 타임라인에 반환한다.")
    @Test
    void findAllVotes() {
        // given
        Long voteId = 1L;
        List<Vote> votes = new ArrayList<>();

        User user = createUser();

        Vote vote1 = createVote(user, "title1", "content1");
        Vote vote2 = createVote(user, "title2", "content2");

        votes.add(vote1);
        votes.add(vote2);

        // 단위 테스트를 위해서 vote 의 null 값에 채워주는 용도
        ReflectionTestUtils.setField(vote1, "id", 1L);
        ReflectionTestUtils.setField(vote2, "id", 2L);

        given(voteRepository.findAll()).willReturn(votes);

        // when
        List<VoteDto> voteDtos = voteService.findAllVotes();

        // then
        assertThat(votes.get(0))
                .hasFieldOrPropertyWithValue("title", vote1.getTitle())
                .hasFieldOrPropertyWithValue("content", vote1.getContent())
                .hasFieldOrPropertyWithValue("category", vote1.getCategory())
                .hasFieldOrPropertyWithValue("createAt", vote1.getCreateAt())
                .hasFieldOrPropertyWithValue("modifiedAt", vote1.getModifiedAt())
                .hasFieldOrPropertyWithValue("isMultiPick", vote1.isMultiPick())
                .hasFieldOrPropertyWithValue("displayRange", vote1.getDisplayRange());

        assertThat(voteDtos.size()).isEqualTo(2);
    }

    private static Vote createVote(User user, String title, String content) {
        return Vote.builder()
                .user(user)
                .title(title)
                .content(content)
                .category(Category.FREE)
                .createAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusDays(3))
                .isMultiPick(true)
                .displayRange(DisplayRange.PUBLIC)
                .build();
    }

    private static User createUser() {
        return User.builder()
                .userId("user")
                .userPassword("password")
                .email("email@email.com")
                .nickname("nick")
                .memo("memo")
                .birthday(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @DisplayName("투표 게시글 정보를 입력하면, 투표 게시글을 저장한다.")
    @Test
    void saveVote() {
        // given
        VoteOptionFormDto voteOptionFormDto1 = createOptionFormDto("option1", "/image/link1");
        VoteOptionFormDto voteOptionFormDto2 = createOptionFormDto("option2", "/image/link2");
        ArrayList<VoteOptionFormDto> voteOptionFormDtos = new ArrayList<>(List.of(voteOptionFormDto1, voteOptionFormDto2));

        VoteForm voteForm = VoteForm.builder()
                .title("new title")
                .content("new content")
                .category(Category.FREE)
                .voteOptions(voteOptionFormDtos)
                .isMultiPick(true)
                .expiredAt(LocalDateTime.now().plusDays(5))
                .createAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .displayRange(DisplayRange.PUBLIC)
                .build();

        UserDto userDto = createUserDto();
        VoteDto voteDto = voteForm.toDto(userDto);
        List<VoteOptionDto> optionDtos = voteOptionFormDtos.stream().map(o -> o.toDto(voteDto)).collect(Collectors.toList());


        // when


        // then

    }

    private static VoteOptionFormDto createOptionFormDto(String content, String imageLink) {
        return VoteOptionFormDto.builder()
                .content(content)
                .imageLink(imageLink)
                .build();
    }

    private static UserDto createUserDto() {
        return UserDto.builder()
                .userId("user")
                .userPassword("password")
                .email("email@email.com")
                .nickname("nick")
                .memo("memo")
                .birthday(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
