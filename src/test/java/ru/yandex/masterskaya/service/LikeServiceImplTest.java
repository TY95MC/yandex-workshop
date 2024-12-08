package ru.yandex.masterskaya.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.masterskaya.client.EventClient;
import ru.yandex.masterskaya.client.RegistrationClient;
import ru.yandex.masterskaya.exception.ConflictException;
import ru.yandex.masterskaya.model.dto.CreateReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.model.dto.client.EventDto;
import ru.yandex.masterskaya.model.dto.client.RegistrationDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class LikeServiceImplTest {

    private final LikeService likeService;
    private final ReviewService reviewService;

    @MockBean
    private final RegistrationClient registrationClient;
    @MockBean
    private final EventClient eventClient;

    CreateReviewDto createReviewDto = new CreateReviewDto(
            1L,
            "username",
            "title",
            "content",
            5,
            1L
    );

    Long userId = 2L;

    EventDto event = new EventDto(1L,
            "event name",
            "event description",
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(2),
            "event location",
            100L
    );


    @Test
    @SneakyThrows
    void shouldAddLikeToReviewSuccess() {
        Mockito
                .when(eventClient.getEventById(anyLong()))
                .thenReturn(Optional.of(event));

        Mockito
                .when(registrationClient.getStatusByEventIdAndUserId(anyLong(), anyLong()))
                .thenReturn(new RegistrationDto("APPROVED"));

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
        Mockito
                .when(eventClient.getEventById(anyLong()))
                .thenReturn(Optional.of(event));

        Mockito
                .when(registrationClient.getStatusByEventIdAndUserId(anyLong(), anyLong()))
                .thenReturn(new RegistrationDto("APPROVED"));

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
        Mockito
                .when(eventClient.getEventById(anyLong()))
                .thenReturn(Optional.of(event));

        Mockito
                .when(registrationClient.getStatusByEventIdAndUserId(anyLong(), anyLong()))
                .thenReturn(new RegistrationDto("APPROVED"));

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
        Mockito
                .when(eventClient.getEventById(anyLong()))
                .thenReturn(Optional.of(event));

        Mockito
                .when(registrationClient.getStatusByEventIdAndUserId(anyLong(), anyLong()))
                .thenReturn(new RegistrationDto("APPROVED"));

        ReviewFullDto reviewFullDto = reviewService.createReview(createReviewDto);
        ReviewFullDto reviewFullDtoWithOneDisLike = likeService.addDislike(reviewFullDto.getId(), userId);

        assertEquals(0, reviewFullDtoWithOneDisLike.getNumberOfLikes());
        assertEquals(1L, reviewFullDtoWithOneDisLike.getNumberOfDisLikes());
    }

    @Test
    @SneakyThrows
    void shouldRemoveDisLikeFromReview() {
        Mockito
                .when(eventClient.getEventById(anyLong()))
                .thenReturn(Optional.of(event));

        Mockito
                .when(registrationClient.getStatusByEventIdAndUserId(anyLong(), anyLong()))
                .thenReturn(new RegistrationDto("APPROVED"));

        ReviewFullDto reviewFullDto = reviewService.createReview(createReviewDto);
        ReviewFullDto reviewFullDtoWithOneDisLike = likeService.addDislike(reviewFullDto.getId(), userId);
        ReviewFullDto reviewFullDtoWithNoDisLike = likeService.removeDislike(reviewFullDto.getId(), userId);

        assertEquals(0, reviewFullDtoWithNoDisLike.getNumberOfLikes());
        assertEquals(0, reviewFullDtoWithNoDisLike.getNumberOfDisLikes());
    }

}
