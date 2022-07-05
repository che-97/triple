package com.eun.triple.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends Base {

    @Column(length = 30)
    private String displayId;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @Builder
    public User(String displayId) {
        this.displayId = displayId;
    }
}
