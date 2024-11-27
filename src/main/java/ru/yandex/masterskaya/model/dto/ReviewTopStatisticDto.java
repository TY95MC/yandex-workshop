package ru.yandex.masterskaya.model.dto;

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
    private List<ReviewFullDto> bestReview;
    private List<ReviewFullDto> badReview;

    @Override
    public String toString() {
        return "ReviewTopStatisticDto{" +
                "bestReview=" + bestReview +
                ", badReview=" + badReview +
                '}';
    }
}
