package com.eun.triple.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eun.triple.constant.ErrorCode;
import com.eun.triple.dto.ReviewRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ReviewController reviewController;

    @Transactional
    @DisplayName("[API][POST] 리뷰 저장 수정 및 삭제하고 성공여부 리턴")
    @Test
    void givenReviewDetails_whenAddAndModAndDelete_thenSuccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Given
        ReviewRequest reviewRequest = ReviewRequest.of(
            "REVIEW",
            "ADD",
            "240a0658-dc5f-4878-9381-ebb7b2667772",
            "좋아요!",
            new String[]{"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8",
                "afb0cef2-851d-4a50-bb07-9cc15cbdc332"},
            "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
        );

        // When & Then
        //저장
        mvc.perform(
                post("/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(reviewRequest))
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));

        //수정
        reviewRequest.setAction("MOD");
        reviewRequest.setContent("good!");
        mvc.perform(
                post("/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(reviewRequest))
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));

        //삭제
        reviewRequest.setAction("DELETE");
        mvc.perform(
                post("/events")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(reviewRequest))
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }

    @Transactional
    @DisplayName("[API][GET] 리뷰 저장하고 사용자별 포인트 조회")
    @Test
    void givenReviewDetailsAndAdd_when_thenSuccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Given
        ReviewRequest reviewRequest = ReviewRequest.of(
            "REVIEW",
            "ADD",
            "240a0658-dc5f-4878-9381-ebb7b2667772",
            "좋아요!",
            new String[]{"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8",
                "afb0cef2-851d-4a50-bb07-9cc15cbdc332"},
            "3ede0ef2-92b7-4817-a5f3-0c575361f745",
            "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
        );

        //저장
        mvc.perform(
            post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reviewRequest))
        ).andExpect(status().isOk());

        // When & Then
        mvc.perform(
                get("/user/" + reviewRequest.getUserId() + "/points"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
            .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()))
            .andExpect(jsonPath("$.data.id").value(reviewRequest.getUserId()))
            .andExpect(jsonPath("$.data.totalPoint").value("3"))
            .andExpect(jsonPath("$.data.pointHistoryList.length()").value("3"));
    }
}