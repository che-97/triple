package com.eun.triple.dto;

import com.eun.triple.entity.Point;
import com.eun.triple.entity.Review;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PointHistoryDto {

    private String reviewCreateDate;
    private String reviewUpdateDate;
    private String reviewDeleteYn;
    private String createDate;
    private String updateDate;
    private String type;
    private String message;
    private String deleteYn;

    public static PointHistoryDto fromEntity(Review review, Point point) {
        return PointHistoryDto.builder()
            .reviewCreateDate(dateToString(review.getCreateDate()))
            .reviewUpdateDate(dateToString(review.getUpdateDate()))
            .reviewDeleteYn(review.getDeleteYn())
            .createDate(dateToString(point.getCreateDate()))
            .updateDate(dateToString(point.getUpdateDate()))
            .type(point.getType().name())
            .message(point.getType().getMessage())
            .deleteYn(point.getDeleteYn())
            .build();
    }

    private static String dateToString(LocalDateTime localDateTime) {
        return localDateTime == null ? ""
            : localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
