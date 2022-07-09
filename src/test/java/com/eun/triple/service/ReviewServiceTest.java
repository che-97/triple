package com.eun.triple.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.eun.triple.constant.ErrorCode;
import com.eun.triple.constant.PointType;
import com.eun.triple.dto.ReviewRequest;
import com.eun.triple.dto.UserDto;
import com.eun.triple.entity.Point;
import com.eun.triple.entity.Review;
import com.eun.triple.exception.TripleException;
import com.eun.triple.repository.PointRepository;
import com.eun.triple.repository.ReviewRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private ReviewService reviewService;

    ReviewRequest reviewRequest;

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.standaloneSetup(reviewService).build();

        reviewRequest = ReviewRequest.of(
            "REVIEW",
            "ADD",
            "240a0658-dc5f-4878-9381-ebb7b2667772",
            "좋아요!",
            new String[]{"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8",
                "afb0cef2-851d-4a50-bb07-9cc15cbdc332"},
            "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
        );
    }

    @DisplayName("포인트 부여")
    @Test
    void givenReviewAndPoint_ThenAddPoint() throws Exception {
        // Given
        Review review = reviewRequest.toEntity();

        // When
        ReflectionTestUtils.invokeMethod(reviewService, "addPoint", review, PointType.REVIEW_TEXT);

        // Then
        assertEquals(PointType.REVIEW_TEXT, review.getPoints().get(0).getType());
    }

    @DisplayName("리뷰에 사진 첨부")
    @Test
    void givenReviewAndPhoto_thenAddPhoto() throws Exception {
        // Given
        Review review = reviewRequest.toEntity();

        // When
        ReflectionTestUtils.invokeMethod(reviewService, "addPhotos", review,
            reviewRequest.getAttachedPhotoIds());

        // Then
        assertEquals(2, review.getPhotos().size());
    }

    @DisplayName("[Error] 리뷰 작성시 reviewId가 있으면 이미등록된 reviewId가 있는지 확인후 있으면 Exceoption 발생")
    @Test
    void whenAlreadyId_thenException() throws Exception {
        // Given
        given(reviewRepository.existsById(reviewRequest.getUuIdReviewId())).willReturn(true);

        // When & Then
        TripleException exception = assertThrows(TripleException.class,
            () -> ReflectionTestUtils.invokeMethod(reviewService, "validAdd", reviewRequest));
        assertEquals(ErrorCode.ALREADY_WRITTEN_REVEIW, exception.getErrorCode());
    }

    @DisplayName("[Error]  리뷰 작성시 reviewId가 없으면 이미등록된 userId와 placeId가 있는지 확인후 있으면 Exceoption 발생")
    @Test
    void whenAlreadyUserIdAndPlaceId_thenException() throws Exception {
        // Given
        reviewRequest.setReviewId(null);
        given(reviewRepository.existsByUserIdAndPlaceIdAndDeleteYn(reviewRequest.getUuIdUserId(),
            reviewRequest.getUuIdPlaceId(), "N")).willReturn(true);

        // When & Then
        TripleException exception = assertThrows(TripleException.class,
            () -> ReflectionTestUtils.invokeMethod(reviewService, "validAdd", reviewRequest));
        assertEquals(ErrorCode.ALREADY_WRITTEN_REVEIW, exception.getErrorCode());
    }

    @DisplayName("[Error] 리뷰를 작성한 user가 아니면 Exceoption 발생")
    @Test
    void givenReviewRequest_whenNotEqualReviewUser_thenException() throws Exception {
        // Given
        Review review = Review.builder()
            .userId(UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f744")).build();

        // When & Then
        TripleException exception = assertThrows(TripleException.class,
            () -> ReflectionTestUtils.invokeMethod(reviewService, "validAuthority", review,
                reviewRequest));
        assertEquals(ErrorCode.PERMISSION, exception.getErrorCode());
    }

    @DisplayName("[Error] reviewId로 조회시 리뷰가 존재하지 않으면 Exceoption 발생")
    @Test
    void givenReviewRequest_whenNotExist_thenException() throws Exception {
        // Given
        given(reviewRepository.findById(reviewRequest.getUuIdReviewId())).willReturn(
            Optional.empty());

        // When & Then
        TripleException exception = assertThrows(TripleException.class,
            () -> ReflectionTestUtils.invokeMethod(reviewService, "getReviewEntity",
                reviewRequest));
        assertEquals(ErrorCode.REVIEW_NOT_EXIST, exception.getErrorCode());
    }

    @DisplayName("reviewId로 조회시 리뷰가 존재하면 Review 반환")
    @Test
    void givenReviewRequest_whenExist_thenReturnReview() throws Exception {

        // Given
        Optional<Review> optionalReview = Optional.of(
            Review.builder().id(reviewRequest.getUuIdReviewId()).build());
        given(reviewRepository.findById(reviewRequest.getUuIdReviewId())).willReturn(
            optionalReview);

        // When & Then
        Review review = ReflectionTestUtils.invokeMethod(reviewService, "getReviewEntity",
            reviewRequest);
        assertEquals(optionalReview.get(), review);
    }

    @DisplayName("리뷰 사진 전부 삭제시 사진 포인트 제거")
    @Test
    void givenReview_thenDeletePhotoPoint() throws Exception {
        // Given
        Review review = Review.builder().id(reviewRequest.getUuIdReviewId()).build();
        review.addPoints(Point.builder().type(PointType.REVIEW_PHOTO).deleteYn("N").build());

        // When & Then
        ReflectionTestUtils.invokeMethod(reviewService, "deletePhotoPoint", review);
        assertEquals("Y", review.getPoints().get(0).getDeleteYn());
    }

    @DisplayName("리뷰 사진 첨부시 사진포인트 적립내역이 없으면 적립")
    @Test
    void givenReview_whenNotHistory_thenAddPhotoPoint() throws Exception {
        // Given
        Review review = Review.builder().id(reviewRequest.getUuIdReviewId()).build();

        // When & Then
        ReflectionTestUtils.invokeMethod(reviewService, "addPhotoPoint", review);
        assertEquals(PointType.REVIEW_PHOTO, review.getPoints().get(0).getType());
    }

    @DisplayName("리뷰 사진 첨부시 사진포인트 적립내역이 있으면 적립하지 않음")
    @Test
    void givenReview_whenHistory_thenNotAddPhotoPoint() throws Exception {
        // Given
        Review review = Review.builder().id(reviewRequest.getUuIdReviewId()).build();
        review.addPoints(Point.builder().type(PointType.REVIEW_PHOTO).build());

        // When & Then
        ReflectionTestUtils.invokeMethod(reviewService, "addPhotoPoint", review);
        assertEquals(1, review.getPoints().size());
    }

    @DisplayName("리뷰 사진 첨부시 사진포인트 적립내역이 있으나 삭제된 이력이면 새로 적립함")
    @Test
    void givenReview_whenDeleteHistory_thenAddPhotoPoint() throws Exception {
        // Given
        Review review = Review.builder().id(reviewRequest.getUuIdReviewId()).build();
        review.addPoints(Point.builder().type(PointType.REVIEW_PHOTO).deleteYn("Y").build());

        // When & Then
        ReflectionTestUtils.invokeMethod(reviewService, "addPhotoPoint", review);
        assertEquals(2, review.getPoints().size());
        assertEquals(null, review.getPoints().get(1).getDeleteYn());
    }

    @DisplayName("userId로 Point 조회")
    @Test
    void givenUserId_thenReturnPointList() throws Exception {
        // Given
        Review review = Review.builder().deleteYn("N").build();
        Review review2 = Review.builder().deleteYn("Y").build();

        List<Point> points = Arrays.asList(
            Point.builder().type(PointType.REVIEW_TEXT).deleteYn("N").review(review).build(),
            Point.builder().type(PointType.REVIEW_PHOTO).deleteYn("Y").review(review).build(),
            Point.builder().type(PointType.REVIEW_FIRST_TIME).deleteYn("N").review(review).build(),
            Point.builder().type(PointType.REVIEW_TEXT).deleteYn("N").review(review2).build()
        );

        given(pointRepository.findAllByUserIdOrderByCreateDateDesc(
            reviewRequest.getUuIdUserId())).willReturn(points);

        // When & Then
        UserDto userDto = ReflectionTestUtils.invokeMethod(reviewService, "getUserPoints",
            reviewRequest.getUserId());
        assertEquals(2, userDto.getTotalPoint());
        assertEquals(4, userDto.getPointHistoryList().size());
        assertEquals(PointType.REVIEW_TEXT.name(), userDto.getPointHistoryList().get(0).getType());
    }
}