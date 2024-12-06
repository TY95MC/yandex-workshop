package ru.yandex.masterskaya.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReviewStatisticDto {
    @Schema(description = "средняя оценка", example = "3,5")
    private double averageMark;

    @Schema(description = "количество отзывов", example = "16")
    private int countReview;

    @Schema(description = "процент хороших отзывов", example = "75")
    private int percentGoodReview;

    @Schema(description = "процент плохих отзывов", example = "25")
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
