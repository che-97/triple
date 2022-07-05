package com.eun.triple.entity;

import java.util.ArrayList;
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
public class Place extends Base {

    @Column(length = 200)
    private String address;

    @OneToMany(mappedBy = "place")
    private List<Review> reviews = new ArrayList<>();

    @Builder
    public Place(String address) {
        this.address = address;
    }
}
