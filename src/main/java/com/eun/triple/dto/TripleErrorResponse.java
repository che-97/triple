package com.eun.triple.dto;

import com.eun.triple.constant.ErrorCode;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TripleErrorResponse {

    private final Boolean success;
    private final Integer errorCode;
    private final String message;

    public static TripleErrorResponse of(Boolean success, Integer errorCode, String message) {
        return new TripleErrorResponse(success, errorCode, message);
    }
}
