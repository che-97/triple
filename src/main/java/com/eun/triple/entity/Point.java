package com.eun.triple.entity;

import com.eun.triple.constant.PointType;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Getter
public class Point extends Base {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "review_id", foreignKey = @ForeignKey(name = "fk_point_review_id"))
    private Review review;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PointType type;

    @Column(length = 1, columnDefinition = "varchar(1) default 'N'")
    private String deleteYn;

    @Builder
    public Point(Review review, PointType type, String deleteYn) {
        this.review = review;
        this.type = type;
        this.deleteYn = deleteYn;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public void delete() {
        this.deleteYn = "Y";
    }

    public boolean isDelete() {
        return this.deleteYn != null && this.deleteYn.equals("Y");
    }

    public boolean hasType(PointType pointType) {
        return type.equals(pointType) && !isDelete();
    }
}
