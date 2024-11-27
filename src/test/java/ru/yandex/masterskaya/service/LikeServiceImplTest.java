package ru.yandex.masterskaya.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.masterskaya.exception.ConflictException;
import ru.yandex.masterskaya.model.dto.CreateReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LikeServiceImplTest {

    private final LikeService likeService;
    private final ReviewService reviewService;

    CreateReviewDto createReviewDto = new CreateReviewDto(
            1L,
            "username",
            "title",
            "content",
            5,
            1L
    );

    Long userId = 2L;


    @Test
    @SneakyThrows
    void shouldAddLikeToReviewSuccess() {
        ReviewFullDto reviewFullDto = reviewService.createReview(createReviewDto);
        ReviewFullDto reviewFullDtoWithLikes = likeService.addLike(reviewFullDto.getId(), userId);
        assertEquals(1L, reviewFullDtoWithLikes.getId());
        assertEquals(reviewFullDto.getTitle(), reviewFullDtoWithLikes.getTitle());
        assertEquals(1L, reviewFullDtoWithLikes.getNumberOfLikes());
        assertEquals(0, reviewFullDtoWithLikes.getNumberOfDisLikes());

    }

    @Test
    @SneakyThrows
    void shouldAddLikeToReviewNotSuccessSamePerson() {
        userId = 1L;

        ReviewFullDto reviewFullDto = reviewService.createReview(createReviewDto);

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            likeService.addLike(reviewFullDto.getId(), userId);
        });

        assertEquals("Автор отзыва не может ставить себе лайк", exception.getMessage());
    }


    @Test
    @SneakyThrows
    void shouldRemoveLikeFromReview() {
        ReviewFullDto reviewFullDto = reviewService.createReview(createReviewDto);
        ReviewFullDto reviewFullDtoWithLikes = likeService.addLike(reviewFullDto.getId(), userId);

        assertEquals(1L, reviewFullDtoWithLikes.getNumberOfLikes());

        ReviewFullDto reviewFullDtoWithNoLikes = likeService.removeLike(reviewFullDto.getId(), userId);

        assertEquals(0, reviewFullDtoWithNoLikes.getNumberOfLikes());
        assertEquals(0, reviewFullDtoWithNoLikes.getNumberOfDisLikes());
    }

    @Test
    @SneakyThrows
    void shouldAddDisLikeToReview() {
        ReviewFullDto reviewFullDto = reviewService.createReview(createReviewDto);
        ReviewFullDto reviewFullDtoWithOneDisLike = likeService.addDislike(reviewFullDto.getId(), userId);

        assertEquals(0, reviewFullDtoWithOneDisLike.getNumberOfLikes());
        assertEquals(1L, reviewFullDtoWithOneDisLike.getNumberOfDisLikes());
    }

    @Test
    @SneakyThrows
    void shouldRemoveDisLikeFromReview() {
        ReviewFullDto reviewFullDto = reviewService.createReview(createReviewDto);
        ReviewFullDto reviewFullDtoWithOneDisLike = likeService.addDislike(reviewFullDto.getId(), userId);
        ReviewFullDto reviewFullDtoWithNoDisLike = likeService.removeDislike(reviewFullDto.getId(), userId);

        assertEquals(0, reviewFullDtoWithNoDisLike.getNumberOfLikes());
        assertEquals(0, reviewFullDtoWithNoDisLike.getNumberOfDisLikes());
    }

}
