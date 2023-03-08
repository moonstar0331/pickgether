package com.capstone.pick.security;

import com.capstone.pick.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotePrincipal implements UserDetails {

    private String username;
    private String password;
    private final String authority ="ROLE_USER";
    private String email;
    private String nickname;
    private LocalDateTime birthday;
    private String memo;

    public static VotePrincipal from(UserDto dto) {
        return VotePrincipal.builder()
                .username(dto.getUserId())
                .password(dto.getUserPassword())
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .birthday(dto.getBirthday())
                .memo(dto.getMemo())
                .build();
    }

    public UserDto toDto() {
        return UserDto.builder()
                .userId(username)
                .userPassword(password)
                .email(email)
                .nickname(nickname)
                .birthday(birthday)
                .memo(memo)
                .build();
    }

    @Override
    public String getPassword() {
        return username;
    }

    @Override
    public String getUsername() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) () -> authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
