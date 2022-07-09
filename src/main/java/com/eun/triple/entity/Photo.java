package com.eun.triple.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Photo extends Base {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "review_id", foreignKey = @ForeignKey(name = "fk_photo_review_id"))
    private Review review;

    @Column(updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID attachedPhotoId;

    @Builder
    public Photo(Review review, UUID attachedPhotoId) {
        this.review = review;
        this.attachedPhotoId = attachedPhotoId;
    }

    public void setReview(Review review) {
        this.review = review;
    }

}
