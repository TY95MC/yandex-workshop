package ru.yandex.masterskaya.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.masterskaya.constants.Constants.DATE_TIME_FORMAT;

@Data
@Builder
public class ApiError {
    private List<String> errors;

    private String message;

    private String reason;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime timestamp;
}
