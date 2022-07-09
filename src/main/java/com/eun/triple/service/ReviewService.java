package com.eun.triple.service;

import com.eun.triple.constant.ErrorCode;
import com.eun.triple.constant.EventAction;
import com.eun.triple.constant.PointType;
import com.eun.triple.dto.PointHistoryDto;
import com.eun.triple.dto.ReviewRequest;
import com.eun.triple.dto.UserDto;
import com.eun.triple.entity.Photo;
import com.eun.triple.entity.Point;
import com.eun.triple.entity.Review;
import com.eun.triple.exception.TripleException;
import com.eun.triple.repository.PointRepository;
import com.eun.triple.repository.ReviewRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PointRepository pointRepository;

    @Transactional
    public void events(ReviewRequest reviewRequest) {
        if (EventAction.isAdd(reviewRequest.getAction())) {
            add(reviewRequest);
        } else if (EventAction.isMod(reviewRequest.getAction())) {
            mod(reviewRequest);
        } else if (EventAction.isDelete(reviewRequest.getAction())) {
            delete(reviewRequest);
        } else {
            throw new TripleException(ErrorCode.UNDEFINED_ACTION);
        }
    }

    private void add(ReviewRequest reviewRequest) {
        validAdd(reviewRequest);

        Review review = reviewRequest.toEntity();

        addPoint(review, PointType.REVIEW_TEXT);

        if (reviewRequest.getAttachedPhotoIds().length > 0) {
            addPhotos(review, reviewRequest.getAttachedPhotoIds());
            addPoint(review, PointType.REVIEW_PHOTO);
        }
        if (!reviewRepository.existsByPlaceIdAndDeleteYn(reviewRequest.getUuIdPlaceId(), "N")) {
            review.addPoints(Point.builder().type(PointType.REVIEW_FIRST_TIME).build());
        }
        reviewRepository.save(review);
    }

    private void validAdd(ReviewRequest reviewRequest) {
        if(reviewRequest.isUserIdEmpty() || reviewRequest.isPlaceIdEmpty()){
            throw new TripleException(ErrorCode.NO_REQUIRED_VALUE);
        }

        if (!reviewRequest.isReviewIdEmpty() && reviewRepository.existsById(reviewRequest.getUuIdReviewId())) {
            throw new TripleException(ErrorCode.ALREADY_WRITTEN_REVEIW);
        }

        if (reviewRepository.existsByUserIdAndPlaceId(reviewRequest.getUuIdUserId(), reviewRequest.getUuIdPlaceId())) {
            throw new TripleException(ErrorCode.ALREADY_WRITTEN_REVEIW);
        }
    }

    private void addPoint(Review review, PointType pointType) {
        review.addPoints(Point.builder().type(pointType).build());
    }

    private void addPhotos(Review review, String[] attachedPhotoIds) {
        Arrays.stream(attachedPhotoIds)
            .forEach(s -> review.addPhotos(
                Photo.builder().attachedPhotoId(UUID.fromString(s)).build()));
    }

    private void mod(ReviewRequest reviewRequest) {
        Review review = getReviewEntity(reviewRequest);

        validAuthority(review, reviewRequest);

        if (reviewRequest.getAttachedPhotoIds().length == 0) {
            deletePhotoPoint(review);
        } else {
            addPhotoPoint(review);
        }
        review.getPhotos().clear();
        addPhotos(review, reviewRequest.getAttachedPhotoIds());

        review.update(reviewRequest.getContent());
    }

    private void deletePhotoPoint(Review review) {
        review.getPoints().stream()
            .forEach(point -> {
                if (point.hasType(PointType.REVIEW_PHOTO)) {
                    point.delete();
                }
            });
    }

    private void addPhotoPoint(Review review) {
        if (!review.getPoints().stream().anyMatch(
            point -> point.hasType(PointType.REVIEW_PHOTO))) {
            addPoint(review, PointType.REVIEW_PHOTO);
        }
    }

    private void delete(ReviewRequest reviewRequest) {
        Review review = getReviewEntity(reviewRequest);

        //review의 delete yn을 n으로 변경
        review.delete();

        //해당 장소에 새로운 첫 리뷰 대상자 확인 후 업데이트
        if (review.getPoints().stream()
            .anyMatch(point -> point.hasType(PointType.REVIEW_FIRST_TIME))) {
            Optional<Review> newBonusReview = reviewRepository.findFirstByPlaceIdAndDeleteYnAndIdNot(
                reviewRequest.getUuIdPlaceId(), "N", reviewRequest.getUuIdReviewId(),
                Sort.by(Sort.Order.asc("createDate")));
            newBonusReview.ifPresent(newReview -> addPoint(newReview, PointType.REVIEW_FIRST_TIME));
        }

    }

    private Review getReviewEntity(ReviewRequest reviewRequest) {
        return reviewRepository.findById(UUID.fromString(reviewRequest.getReviewId()))
            .orElseThrow(() -> new TripleException(ErrorCode.REVIEW_NOT_EXIST));
    }

    private void validAuthority(Review review, ReviewRequest reviewRequest) {
        if (!review.getUserId().toString().equals(reviewRequest.getUserId())) {
            throw new TripleException(ErrorCode.PERMISSION);
        }
    }

    public UserDto getUserPoints(String userId) {
        List<Point> points = pointRepository.findAllByUserIdOrderByCreateDateDesc(
            UUID.fromString(userId));
        UserDto userDto = UserDto.builder().id(UUID.fromString(userId)).build();
        points.stream().forEach(point -> {
            userDto.addPoint(PointHistoryDto.fromEntity(point.getReview(), point));
            userDto.addTotalPoint(point.getReview(), point);
        });

        return userDto;
    }

}
