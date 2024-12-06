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
public class UpdateReviewDto {
    @Schema(description = "никнейм автора отзыва", example = "nickname")
    private String username;

    @Length(max = 100)
    @Schema(description = "обновленный заголовок отзыва", example = "Это просто космос!")
    private String title;

    @Length(max = 1500)
    @Schema(description = "обновленный текст отзыва", example = "Я просто на 7 небе...")
    private String content;

    @Min(1)
    @Max(10)
    @Schema(description = "обновленная оценка проведенного ивента", example = "10")
    private Integer mark;
}
