package com.eun.triple.entity;

import com.eun.triple.constant.PointType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Point extends Base {

    @ManyToOne
    @JoinColumn(name = "review_id", foreignKey = @ForeignKey( name = "fk_point_review_id"))
    private Review review;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private PointType type;

    @Column(length = 1, columnDefinition = "varchar(1) default 'N'")
    private String deleteYn;

    @Builder
    public Point(Review review, PointType type, String deleteYn) {
        this.review = review;
        this.type = type;
        this.deleteYn = deleteYn;

    }
}
