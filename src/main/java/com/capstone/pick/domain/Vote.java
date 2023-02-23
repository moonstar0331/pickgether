package com.capstone.pick.domain;

import com.capstone.pick.domain.constant.Category;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Entity
public class Vote {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long id; // 투표 게시글 id

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user; // 유저 정보 (id)

    @Column(length = 100)
    private String title; // 제목

    @Column(length = 100)
    private String content; // 내용

    @Enumerated(EnumType.STRING)
    private Category category; // 카테고리

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime expiredAt; // 종료일시

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt; // 생성일시

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일시

    private boolean isMultiPick; // 다중선택가능여부

    protected Vote() {}

    private Vote(User user, String title, String content, Category category, LocalDateTime expiredAt, boolean isMultiPick) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.category = category;
        this.expiredAt = expiredAt;
        this.isMultiPick = isMultiPick;
    }

    public static Vote of(User user, String title, String content, Category category, LocalDateTime expiredAt, boolean isMultiPick) {
        return new Vote(user, title, content, category, expiredAt, isMultiPick);
    }
}
