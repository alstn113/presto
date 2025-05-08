package com.presto.server.user.domain;

import com.presto.server.common.persistence.IdentifiableEntity;
import com.presto.server.common.persistence.Timestamps;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends IdentifiableEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Embedded
    private Timestamps timestamps;

    public Member(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
