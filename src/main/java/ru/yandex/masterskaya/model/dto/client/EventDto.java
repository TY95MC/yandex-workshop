package ru.yandex.masterskaya.model.dto.client;

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
public class EventDto {
    @Schema(description = "id ивента", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "название ивента", example = "гала концерт", accessMode = Schema.AccessMode.READ_ONLY)
    private String name;

    @Schema(description = "описание ивента", example = "большой концерт посвященный пику творчества", accessMode = Schema.AccessMode.READ_ONLY)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @Schema(description = "начало ивента", example = "2024-12-21 14:32:47", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime startDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @Schema(description = "конец ивента", example = "2024-12-21 14:33:47", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime endDateTime;

    @Schema(description = "место ивента", example = "г. Владивосток, ул. Юбилейная 69", accessMode = Schema.AccessMode.READ_ONLY)
    private String location;

    @Schema(description = "id организатора", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long ownerId;
}
