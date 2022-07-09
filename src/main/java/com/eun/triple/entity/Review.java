package com.eun.triple.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DynamicInsert
@Table(indexes = {@Index(name = "fk_review_user_id", columnList = "userId"),
    @Index(name = "fk_review_place_id", columnList = "placeId")})
public class Review extends Base {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID placeId;

    @Column
    @Lob
    private String content;

    @Column(length = 1, columnDefinition = "varchar(1) default 'N'")
    private String deleteYn;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Point> points = new ArrayList<>();

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            id = UUID.randomUUID();
        }

    }

    @Builder
    public Review(UUID id, UUID userId, UUID placeId, String content, String deleteYn) {
        this.id = id;
        this.userId = userId;
        this.placeId = placeId;
        this.content = content;
        this.deleteYn = deleteYn;
    }

    public void delete() {
        this.deleteYn = "Y";
    }

    public void update(String content) {
        this.content = content;
    }

    public void addPoints(Point point) {
        points.add(point);
        point.setReview(this);
    }

    public void addPhotos(Photo photo) {
        photos.add(photo);
        photo.setReview(this);
    }

    public boolean isDelete() {
        return this.deleteYn != null && this.deleteYn.equals("Y");
    }

}
