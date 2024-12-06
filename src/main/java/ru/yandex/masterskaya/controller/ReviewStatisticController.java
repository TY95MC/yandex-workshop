package ru.yandex.masterskaya.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.masterskaya.model.dto.ReviewStatisticDto;
import ru.yandex.masterskaya.model.dto.ReviewTopStatisticDto;
import ru.yandex.masterskaya.service.ReviewStatisticService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/statistic")
@Tag(name = "Статистика отзывов", description = "Статистика по ивенту, пользователю")
public class ReviewStatisticController {
    private final ReviewStatisticService reviewStatisticService;

    @GetMapping("/{eventId}")
    @Operation(summary = "Получение статистики отзывов ивента",
            description = "Получение средней оценки ивента, количества отзывов, процента хороших и плохих отзывов")
    public ReviewStatisticDto getStatistic(@PathVariable @Min(1)
                                           @Parameter(description = "id ивента", example = "1") Long eventId) {
        return reviewStatisticService.getStatisticByEvent(eventId);
    }

    @GetMapping("/top/{eventId}")
    @Operation(summary = "Получение топ-статистики отзывов ивента",
            description = "Получение 3 лучших и 3 худших отзывов ивента")
    public ReviewTopStatisticDto getTopStatistic(@PathVariable @Min(1)
                                                 @Parameter(description = "id ивента", example = "1") Long eventId) {
        return reviewStatisticService.getTopStatisticByEvent(eventId);
    }

    @GetMapping("/top/author/{authorId}")
    @Operation(summary = "Получение статистики отзывов пользователя",
            description = "Получение средней оценки пользователя, количества отзывов, процента хороших и плохих отзывов")
    public ReviewStatisticDto getStatisticByAuthor(@PathVariable @Min(1)
                                                   @Parameter(description = "id автора", example = "1") Long authorId) {
        return reviewStatisticService.getStatisticByUser(authorId);
    }
}
