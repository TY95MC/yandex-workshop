package ru.yandex.masterskaya.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.service.LikeService;

import static ru.yandex.masterskaya.constants.Constants.X_REVIEW_USER_ID;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{reviewId}/like")
    public ReviewFullDto addLike(@PathVariable @Min(1) Long reviewId,
                                 @RequestHeader(X_REVIEW_USER_ID) @NotNull @Min(1) Long userId) {
        return likeService.addLike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like")
    public ReviewFullDto removeLike(@PathVariable @Min(1) Long reviewId,
                                    @RequestHeader(X_REVIEW_USER_ID) @Min(1) Long userId) {
        return likeService.removeLike(reviewId, userId);
    }

    @PostMapping("/{reviewId}/dislike")
    public ReviewFullDto addDislike(@PathVariable() @Min(1) Long reviewId,
                                    @RequestHeader(X_REVIEW_USER_ID) @Min(1) Long userId) {
        return likeService.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike")
    public ReviewFullDto removeDislike(@PathVariable @Min(1) Long reviewId,
                                       @RequestHeader(X_REVIEW_USER_ID) @Min(1) Long userId) {
        return likeService.removeDislike(reviewId, userId);
    }





}
