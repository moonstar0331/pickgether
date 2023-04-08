package com.capstone.pick.domain;

import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class User {

    @Id @Column(length = 50, name = "user_id")
    private String userId; // 유저 Id

    @Column(nullable = false)
    private String userPassword; // 유저 암호

    @Column(length = 100)
    private String email; // 이메일

    @Column(length = 100)
    private String nickname; // 닉네임

    private String birthday; // 출생일시

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일시

    private String memo; // 메모

    private String gender; // 성별
    private String age_range; // 연령대

    private String provider; // 가입방식

    private String job;
    private String address;

    /* TODO: 가입방식과 멤버십은 주요 기능이 완료된 후에
         멤버십과 소셜 로그인 기능을 구현한 후에 진행하도록 한다.
    private String membership; // 멤버십
    */

    @OneToMany(mappedBy = "fromUser")
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "toUser")
    private List<Follow> followers = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getUserId().equals(user.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }

    public void updateInfo(String gender,String age_range,String job,String address){
        this.gender = gender;
        this.age_range = age_range;
        this.job = job;
        this.address = address;
    }
}
