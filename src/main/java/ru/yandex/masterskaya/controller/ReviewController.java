package ru.yandex.masterskaya.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.masterskaya.model.dto.CreateReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.model.dto.UpdateReviewDto;
import ru.yandex.masterskaya.service.ReviewService;

import static ru.yandex.masterskaya.constants.Constants.X_REVIEW_USER_ID;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    private final ReviewService service;

    @PostMapping
    public ReviewFullDto create(@RequestBody @Valid CreateReviewDto dto) {
        return service.createReview(dto);
    }

    @PatchMapping("/{id}")
    public ReviewFullDto update(@RequestHeader(X_REVIEW_USER_ID) @Min(1) Long authorId,
                                @PathVariable(value = "id") @Min(1) Long id,
                                @RequestBody @Valid UpdateReviewDto dto) {
        return service.updateReview(authorId, id, dto);
    }

    @GetMapping("/{id}")
    public ReviewDto get(@PathVariable(value = "id") @Min(1) Long id) {
        return service.getReview(id);
    }

    @GetMapping
    public Slice<ReviewDto> getRevs(@RequestParam(value = "page", required = false, defaultValue = "0") @Min(0) int page,
                                    @RequestParam(value = "size", required = false, defaultValue = "5")
                                    @Min(1) @Max(50) int size,
                                    @RequestParam(value = "eventId") @Min(1) Long eventId) {
        return service.getReviews(page, size, eventId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestHeader(X_REVIEW_USER_ID) @Min(1) Long authorId,
                       @PathVariable(value = "id") @Min(1) Long id) {
        service.delete(authorId, id);
    }
}
