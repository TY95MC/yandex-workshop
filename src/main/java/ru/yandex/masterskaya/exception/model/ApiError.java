package ru.yandex.masterskaya.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.masterskaya.constants.Constants.DATE_TIME_FORMAT;

@Data
@Builder
public class ApiError {
    @Schema(description = "список ошибок")
    private List<String> errors;

    @Schema(description = "текст ошибки")
    private String message;

    @Schema(description = "причина ошибки")
    private String reason;

    @Schema(description = "статус ошибки")
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @Schema(description = "дата ошибки", example = "2024-12-21 14:32:47", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime timestamp;
}
