package com.eun.triple.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Review {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @CreatedDate
    @Column(updatable = false, columnDefinition = "DATETIME default CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_review_user_id"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_id", foreignKey = @ForeignKey(name = "fk_review_place_id"))
    private Place place;

    @Column
    @Lob
    private String contents;

    @Column(length = 1, columnDefinition = "varchar(1) default 'N'")
    private String deleteYn;

    @OneToMany(mappedBy = "review", cascade = {CascadeType.PERSIST})
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "review", cascade = {CascadeType.PERSIST})
    private List<Point> points = new ArrayList<>();

    @PrePersist
    public void generateId() {
        if (this.id == null) {
            id = UUID.randomUUID();
        }

    }

    @Builder
    public Review(UUID id ,User user, Place place, String contents, String deleteYn) {
        this.id = id;
        this.user = user;
        this.place = place;
        this.contents = contents;
        this.deleteYn = deleteYn;
    }
}
