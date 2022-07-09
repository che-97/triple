package com.eun.triple.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    REVIEW;

    public static boolean isReview(String type) {
        return REVIEW.name().equals(type);
    }
}
