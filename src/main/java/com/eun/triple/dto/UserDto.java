package com.eun.triple.dto;

import com.eun.triple.entity.Point;
import com.eun.triple.entity.Review;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {

    private UUID id;

    @Builder.Default
    private int totalPoint = 0;

    @Builder.Default
    private List<PointHistoryDto> pointHistoryList = new ArrayList<>();

    public void addTotalPoint(Review review, Point point) {
        if (!review.isDelete() && !point.isDelete()) {
            this.totalPoint += point.getType().getPoint();
        }
    }

    public void addPoint(PointHistoryDto pointHistoryDto) {
        pointHistoryList.add(pointHistoryDto);
    }


}
