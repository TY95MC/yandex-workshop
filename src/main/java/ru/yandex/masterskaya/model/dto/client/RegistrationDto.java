package ru.yandex.masterskaya.model.dto.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegistrationDto {
    @Schema(description = "статус юзера", example = "APPROVED", accessMode = Schema.AccessMode.READ_ONLY)
    String status;
}
