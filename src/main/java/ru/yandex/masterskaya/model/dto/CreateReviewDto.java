package ru.yandex.masterskaya.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateReviewDto {
    @Min(1)
    @Schema(description = "id автора отзыва", example = "1")
    private Long authorId;

    @Schema(description = "никнейм автора отзыва", example = "nickname")
    private String username;

    @Length(max = 100)
    @Schema(description = "заголовок отзыва", example = "Это просто феерия какая-то!")
    private String title;

    @Length(max = 1500)
    @Schema(description = "текст отзыва", example = "Спешу поделиться своими впечатлениями...")
    private String content;

    @Min(1)
    @Max(10)
    @Schema(description = "оценка проведенного ивента", example = "10")
    private Integer mark;

    @Min(1)
    @Schema(description = "id ивента", example = "1")
    private Long eventId;
}
