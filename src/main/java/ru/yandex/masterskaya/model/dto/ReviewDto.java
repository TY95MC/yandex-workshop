package ru.yandex.masterskaya.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static ru.yandex.masterskaya.constants.Constants.DATE_TIME_FORMAT;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewDto {
    @Schema(description = "id отзыва", example = "1")
    private Long id;

    @Schema(description = "никнейм автора отзыва", example = "nickname")
    private String username;

    @Schema(description = "заголовок отзыва", example = "Это просто феерия какая-то!")
    private String title;

    @Schema(description = "текст отзыва", example = "Спешу поделиться своими впечатлениями...")
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @Schema(description = "дата отзыва", example = "2024-12-21 14:32:47", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @Schema(description = "дата обновления отзыва", example = "2024-12-21 14:32:47", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedDateTime;

    @Schema(description = "оценка проведенного ивента", example = "8")
    private Integer mark;

    @Schema(description = "id ивента", example = "1")
    private Long eventId;
}
