package ru.yandex.masterskaya.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReviewStatisticDto {
    private double averageMark;
    private int countReview;
    private int percentGoodReview;
    private int percentBadReview;

    @Override
    public String toString() {
        return "ReviewStatisticDto{" +
                "averageMark=" + averageMark +
                ", countReview=" + countReview +
                ", percentGoodReview=" + percentGoodReview +
                ", percentBadReview=" + percentBadReview +
                '}';
    }
}
