package com.eun.triple.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.eun.triple.constant.PointType;
import com.eun.triple.dto.ReviewRequest;
import com.eun.triple.entity.Point;
import com.eun.triple.entity.Review;
import java.util.List;
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
@DisplayName("Point Repository Test")
class PointRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private PointRepository pointRepository;

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
    @DisplayName("사용자별 point를 조회한다.")
    public void givenUserId_thenRetrunPointList() {
        // given
        Review review = reviewRequest.toEntity();
        review.addPoints(Point.builder().type(PointType.REVIEW_TEXT).build());
        testEntityManager.persistAndFlush(review);

        // when & then
        final List<Point> poins = pointRepository.findAllByUserIdOrderByCreateDateDesc(
            reviewRequest.getUuIdUserId());

        assertEquals(1, poins.size());
        assertEquals(PointType.REVIEW_TEXT, poins.get(0).getType());
    }

}