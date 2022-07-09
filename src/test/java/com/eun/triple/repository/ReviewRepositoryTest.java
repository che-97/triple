package com.eun.triple.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.eun.triple.constant.PointType;
import com.eun.triple.dto.ReviewRequest;
import com.eun.triple.entity.Point;
import com.eun.triple.entity.Review;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DisplayName("Review 테스트")
class ReviewRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ReviewRepository reviewRepository;

    ReviewRequest reviewRequest;

    @BeforeEach
    public void init() {

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

    @Test
    @DisplayName("리뷰를 저장한다.")
    public void saveReviewTest() {
        Review review = Review.builder().userId(reviewRequest.getUuIdUserId())
            .placeId(reviewRequest.getUuIdPlaceId()).build();

        Review saveReview = reviewRepository.save(review);

        assertThat(entityManager.contains(review)).isTrue();
        assertThat(entityManager.contains(saveReview)).isTrue();
        assertThat(saveReview == review);

        Review postUpdate = reviewRequest.toEntity();
        Review updated = reviewRepository.save(postUpdate);

        assertThat(entityManager.contains(updated)).isTrue();
        assertThat(entityManager.contains(postUpdate)).isFalse();
        assertThat(updated != postUpdate);
    }

    @Test
    @DisplayName("placeId와 삭제여부로 리뷰가 존재하는지 확인")
    public void existsByPlaceIdAndDeleteYnTest() {

        Review review = reviewRequest.toEntity();
        Point textPoint = Point.builder().type(PointType.REVIEW_TEXT).build();
        review.addPoints(textPoint);

        testEntityManager.persist(review);
        assertThat(reviewRepository.existsByPlaceIdAndDeleteYn(reviewRequest.getUuIdPlaceId(),
            "N")).isTrue();
    }

    @Test
    @DisplayName("placeId와 삭제여부로 리뷰가 존재하지 않는지 확인")
    public void notExistsByPlaceIdAndDeleteYnTest() {
        assertThat(reviewRepository.existsByPlaceIdAndDeleteYn(reviewRequest.getUuIdPlaceId(),
            "N")).isFalse();
    }

    @Test
    @DisplayName("리뷰 id로 조회")
    public void findByIdTest() {

        Review review = reviewRequest.toEntity();
        Point textPoint = Point.builder().type(PointType.REVIEW_TEXT).build();
        Point firstPoint = Point.builder().type(PointType.REVIEW_FIRST_TIME).build();

        review.addPoints(textPoint);
        review.addPoints(firstPoint);

        testEntityManager.persist(review);

        Optional<Review> optionalReview = reviewRepository.findById(
            reviewRequest.getUuIdReviewId());

        assertThat(optionalReview.isPresent()).isTrue();
        assertThat(optionalReview.get().getPoints()).contains(textPoint, firstPoint);
    }

    @Test
    @DisplayName("리뷰 id로 리뷰가 존재하는지 확인")
    public void existsByIdTest() {

        Review review = reviewRequest.toEntity();
        Point textPoint = Point.builder().type(PointType.REVIEW_TEXT).build();
        review.addPoints(textPoint);

        testEntityManager.persist(review);
        assertThat(reviewRepository.existsById(reviewRequest.getUuIdReviewId())).isTrue();
    }

    @Test
    @DisplayName("리뷰 id로 리뷰가 존재하지 않는지 확인")
    public void notExistsByIdTest() {
        assertThat(reviewRepository.existsById(reviewRequest.getUuIdReviewId())).isFalse();
    }

    @Test
    @DisplayName("userId와 placeId로 리뷰가 존재하는지 확인")
    public void existsByUserIdAndPlaceId() {

        Review review = reviewRequest.toEntity();
        Point textPoint = Point.builder().type(PointType.REVIEW_TEXT).build();
        review.addPoints(textPoint);

        testEntityManager.persist(review);
        assertThat(reviewRepository.existsByUserIdAndPlaceIdAndDeleteYn(reviewRequest.getUuIdUserId(),
            reviewRequest.getUuIdPlaceId(), "N")).isTrue();
    }

    @Test
    @DisplayName("userId와 placeId로 리뷰가 존재하지 않는지 확인")
    public void notExistsByUserIdAndPlaceId() {
        assertThat(reviewRepository.existsByUserIdAndPlaceIdAndDeleteYn(reviewRequest.getUuIdUserId(),
            reviewRequest.getUuIdPlaceId(),"N")).isFalse();
    }


}