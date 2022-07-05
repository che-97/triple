package com.eun.triple.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointType {
    REVIEW_TEXT(1), REVIEW_PHOTO(1), REVIEW_FIRST_TIME(1);

    private final Integer point;

}
