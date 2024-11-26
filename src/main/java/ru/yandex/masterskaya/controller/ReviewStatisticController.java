package ru.yandex.masterskaya.controller;

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
public class ReviewStatisticController {
    private final ReviewStatisticService reviewStatisticService;

    @GetMapping("/{eventId}")
    public ReviewStatisticDto getStatistic(@PathVariable Long eventId) {
        return reviewStatisticService.getStatisticByEvent(eventId);
    }

    @GetMapping("/top/{eventId}")
    public ReviewTopStatisticDto getTopStatistic(@PathVariable Long eventId) {
        return reviewStatisticService.getTopStatisticByEvent(eventId);
    }

    @GetMapping("/top/author/{authorId}")
    public ReviewStatisticDto getStatisticByAuthor(@PathVariable Long authorId) {
        return reviewStatisticService.getStatisticByUser(authorId);
    }
}
