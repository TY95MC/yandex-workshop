package ru.yandex.masterskaya.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.service.LikeService;

import static ru.yandex.masterskaya.constants.Constants.X_REVIEW_USER_ID;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
@Tag(name = "Лайки", description = "Добавление/удаление лайков/дизлайков")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{reviewId}/like")
    @Operation(summary = "Добавление лайка",
            description = "Если уже установлен дизлайк, то он удаляется и лайк не добавляется")
    public ReviewFullDto addLike(@PathVariable @Min(1)
                                 @Parameter(description = "id отзыва", example = "1") Long reviewId,
                                 @RequestHeader(X_REVIEW_USER_ID) @NotNull @Min(1)
                                 @Parameter(description = "id пользователя", example = "1") Long userId) {
        return likeService.addLike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like")
    @Operation(summary = "Удаление лайка")
    public ReviewFullDto removeLike(@PathVariable @Min(1)
                                    @Parameter(description = "id отзыва", example = "1") Long reviewId,
                                    @RequestHeader(X_REVIEW_USER_ID) @Min(1)
                                    @Parameter(description = "id пользователя", example = "1") Long userId) {
        return likeService.removeLike(reviewId, userId);
    }

    @PostMapping("/{reviewId}/dislike")
    @Operation(summary = "Добавление дизлайка",
            description = "Если установлен лайк, то он удаляется, а дизлайк не добавляется")
    public ReviewFullDto addDislike(@PathVariable() @Min(1)
                                    @Parameter(description = "id отзыва", example = "1") Long reviewId,
                                    @RequestHeader(X_REVIEW_USER_ID) @Min(1)
                                    @Parameter(description = "id пользователя", example = "1") Long userId) {
        return likeService.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike")
    @Operation(summary = "Удаление дизлайка")
    public ReviewFullDto removeDislike(@PathVariable @Min(1)
                                       @Parameter(description = "id отзыва", example = "1") Long reviewId,
                                       @RequestHeader(X_REVIEW_USER_ID) @Min(1)
                                       @Parameter(description = "id пользователя", example = "1") Long userId) {
        return likeService.removeDislike(reviewId, userId);
    }


}
