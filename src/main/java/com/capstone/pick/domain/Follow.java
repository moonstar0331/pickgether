package com.capstone.pick.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@IdClass(Follow.PK.class)
public class Follow {
    @Id
    @Column(name = "from_user_id", insertable = false, updatable = false)
    private String fromUserId;

    @Id
    @Column(name = "to_user_id", insertable = false, updatable = false)
    private String toUserId;

    @NoArgsConstructor
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class PK implements Serializable {
        String fromUserId;
        String toUserId;
    }
}
