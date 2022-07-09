package com.eun.triple.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointType {
    REVIEW_TEXT(1, "텍스트 작성")
    , REVIEW_PHOTO(1, "사진 첨부")
    , REVIEW_FIRST_TIME(1, "첫 리뷰 작성");

    private final Integer point;
    private final String message;

}
