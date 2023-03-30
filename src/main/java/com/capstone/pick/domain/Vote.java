package com.capstone.pick.domain;

import com.capstone.pick.domain.constant.Category;

import lombok.*;

import com.capstone.pick.domain.constant.DisplayRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    @Enumerated(EnumType.STRING)
    private DisplayRange displayRange; // 공개범위

    @ToString.Exclude
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VoteOption> voteOptions = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VoteComment> voteComments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return getId().equals(vote.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public void changeTitle(String updateTitle) {
        this.title = updateTitle;
    }

    public void changeContent(String updateContent) {
        this.content = updateContent;
    }

    public void changeCategory(Category updateCategory) {
        this.category = updateCategory;
    }

    public void changeExpiredAt(LocalDateTime updateExpiredAt) {
        this.expiredAt = updateExpiredAt;
    }

    public void changeIsMultiPick(boolean updateIsMultiPick) {
        this.isMultiPick = updateIsMultiPick;
    }

    public void changeDisplayRange(DisplayRange updateDisplayRange) {
        this.displayRange = updateDisplayRange;
    }
}
