package com.eun.triple.dto;

import com.eun.triple.constant.ErrorCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ReviewDataResponse<T> extends TripleErrorResponse {

    private final T data;

    private ReviewDataResponse(T data) {
        super(true, ErrorCode.OK.getCode(), ErrorCode.OK.getMessage());
        this.data = data;
    }

    public static <T> ReviewDataResponse<T> of(T data) {
        return new ReviewDataResponse<>(data);
    }

    public static <T> ReviewDataResponse<T> empty() {
        return new ReviewDataResponse<>(null);
    }

}
