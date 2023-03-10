package com.capstone.pick.controller.form;

import com.capstone.pick.dto.CommentDto;
import com.capstone.pick.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentForm {

    private Long voteId;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime modifiedAt;

    public CommentDto toDto(UserDto userDto) {
        return CommentDto.builder()
                .voteId(voteId)
                .userDto(userDto)
                .content(content)
                .createAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();
    }
}
