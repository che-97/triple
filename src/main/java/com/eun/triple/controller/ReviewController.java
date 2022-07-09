package com.eun.triple.controller;

import com.eun.triple.constant.ErrorCode;
import com.eun.triple.constant.EventType;
import com.eun.triple.dto.ReviewDataResponse;
import com.eun.triple.dto.ReviewRequest;
import com.eun.triple.dto.UserDto;
import com.eun.triple.exception.TripleException;
import com.eun.triple.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/events")
    public ReviewDataResponse<String> events(@RequestBody final ReviewRequest reviewRequest)
        throws Exception {
        if (EventType.isReview(reviewRequest.getType())) {
            reviewService.events(reviewRequest);
            return ReviewDataResponse.empty();
        }
        throw new TripleException(ErrorCode.UNDEFINED_EVENT_TYPE);
    }

    @GetMapping("/user/{userId}/points")
    public ReviewDataResponse<UserDto> getUserPoints(@PathVariable String userId) throws Exception {
        return ReviewDataResponse.of(reviewService.getUserPoints(userId));
    }
}
