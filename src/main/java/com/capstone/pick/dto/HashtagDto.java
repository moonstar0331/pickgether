package com.capstone.pick.dto;

import com.capstone.pick.domain.Hashtag;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HashtagDto {
    private Long id;
    private String content;

    public static HashtagDto from(Hashtag entity) {
        return HashtagDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .build();
    }

    public Hashtag toEntity() {
        return Hashtag.builder()
                .content(content)
                .build();
    }
}
