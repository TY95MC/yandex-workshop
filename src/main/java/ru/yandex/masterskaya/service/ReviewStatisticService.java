package ru.yandex.masterskaya.service;

import ru.yandex.masterskaya.model.dto.ReviewStatisticDto;
import ru.yandex.masterskaya.model.dto.ReviewTopStatisticDto;

public interface ReviewStatisticService {
    ReviewStatisticDto getStatisticByEvent(Long eventId);

    ReviewTopStatisticDto getTopStatisticByEvent(Long eventId);

    ReviewStatisticDto getStatisticByUser(Long authorId);
}
