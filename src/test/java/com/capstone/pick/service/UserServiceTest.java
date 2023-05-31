package com.capstone.pick.service;

import com.capstone.pick.domain.User;
import com.capstone.pick.domain.constant.SearchType;
import com.capstone.pick.dto.UserDto;
import com.capstone.pick.dto.VoteOptionCommentDto;
import com.capstone.pick.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@DisplayName("유저 서비스 로직")
@ExtendWith(MockitoExtension.class)
@ComponentScan(basePackages = "com.capstone.pick.config")
@MockBean(JpaMetamodelMappingContext.class)
public class UserServiceTest {

    @Spy
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @DisplayName("유저 검색 - 정상 호출")
    @Test
    void findUserById() {
        // given
        String userId = "username";
        given(userRepository.findById(userId)).willReturn(Optional.of(mock(User.class)));

        // when
        userService.findUserById(userId);

        // then
        then(userRepository).should().findById(anyString());
    }

    @DisplayName("유저 검색 - 일치하는 유저가 없을 경우 - 에러 발생")
    @Test
    void findUserById_UsernameNotFound() {
        // given
        String userId = "username";
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.findUserById(userId));
    }

    @DisplayName("검색 - 검색한 user id와 일치하는 내용 유저 프로필 반환")
    @Test
    void searchUsers_USERID() {
        // given
        given(userRepository.findByUserIdContaining("userId")).willReturn(List.of());

        // when
        List<UserDto> users = userService.searchUsers(SearchType.USER, "userId");

        // then
        then(userRepository).should().findByUserIdContaining(anyString());
        assertThat(users.isEmpty()).isTrue();
    }

    @DisplayName("검색 - 검색한 user nickname과 일치하는 내용 유저 프로필 반환")
    @Test
    void searchUsers_NICKNAME() {
        // given
        given(userRepository.findByNicknameContaining("nickname")).willReturn(List.of());

        // when
        List<UserDto> users = userService.searchUsers(SearchType.NICKNAME, "nickname");

        // then
        then(userRepository).should().findByNicknameContaining(anyString());
        assertThat(users.isEmpty()).isTrue();
    }

    @DisplayName("유저 정보 수정")
    @Test
    void editProfile() {
        // given
        User user = User.builder()
                .userId("user")
                .nickname("1")
                .birthday("0101")
                .build();
        UserDto userDto = createUserDto();
        String nick = "nickname";
        String birth = "2001-01-01";
        String gender = "남성";
        String job = "학생";
        String memo = "메모";
        String address = "경기";
        userDto.updateInfo(nick, birth, gender, job, memo, address);

        given(userRepository.save(userDto.toEntity())).willReturn(user);

        // when
        userService.editProfile(userDto, nick, birth, gender, job, memo, address);

        // then
        then(userRepository).should().save(userDto.toEntity());
    }

    private static UserDto createUserDto() {
        return UserDto.builder()
                .userId("user")
                .userPassword("password")
                .email("email@email.com")
                .nickname("nick")
                .memo("memo")
                .birthday("0101")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
