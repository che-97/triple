package com.eun.triple.dto;

import com.eun.triple.entity.Review;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewRequest {

    private String type;
    private String action;
    private String reviewId;
    private String content;
    private String[] attachedPhotoIds;
    private String userId;
    private String placeId;

    public static ReviewRequest of(
        String type,
        String action,
        String reviewId,
        String content,
        String[] attachedPhotoIds,
        String userId,
        String placeId
    ){
        return new ReviewRequest(type, action, reviewId, content, attachedPhotoIds, userId, placeId);
    }

    public Review toEntity() {
        return Review.builder()
            .id(isReviewIdEmpty() ? null : UUID.fromString(reviewId))
            .content(content)
            .userId(UUID.fromString(userId))
            .placeId(UUID.fromString(placeId))
            .build();
    }

    public boolean isReviewIdEmpty() {
        return reviewId == null || reviewId.isEmpty();
    }

    public UUID getUuIdUserId() {
        return UUID.fromString(userId);
    }

    public UUID getUuIdPlaceId() {
        return UUID.fromString(placeId);
    }

    public UUID getUuIdReviewId() {
        return UUID.fromString(reviewId);
    }

    public boolean isUserIdEmpty() {
        return userId == null || userId.isEmpty();
    }

    public boolean isPlaceIdEmpty() {
        return placeId == null || placeId.isEmpty();
    }
}
