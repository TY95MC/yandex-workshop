package ru.yandex.masterskaya.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ReviewTopStatisticDto {
    @Schema(description = "хорошие отзывы")
    private List<ReviewFullDto> bestReview;

    @Schema(description = "плохие отзывы")
    private List<ReviewFullDto> badReview;

    @Override
    public String toString() {
        return "ReviewTopStatisticDto{" +
                "bestReview=" + bestReview +
                ", badReview=" + badReview +
                '}';
    }
}
