package ru.yandex.masterskaya.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import ru.yandex.masterskaya.model.dto.CreateReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.model.dto.UpdateReviewDto;
import ru.yandex.masterskaya.service.LikeService;
import ru.yandex.masterskaya.service.ReviewService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.masterskaya.constants.Constants.X_REVIEW_USER_ID;


@WebMvcTest(controllers = ReviewController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ReviewControllerWithLikesTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private final ReviewService service;
    @MockBean
    private final LikeService likeService;


    CreateReviewDto createReviewDto = new CreateReviewDto(
            1L,
            "username",
            "title",
            "content",
            5,
            1L
    );

    CreateReviewDto createFailDto = new CreateReviewDto(
            0L,
            "username",
            "title",
            "content",
            11,
            0L
    );

    UpdateReviewDto updateReviewDto = new UpdateReviewDto(
            "username",
            "new title",
            "new content",
            3
    );

    UpdateReviewDto updateFailReviewDto = new UpdateReviewDto(
            "username",
            "new title",
            "new content",
            15
    );

    ReviewFullDto savedReviewFullDto = new ReviewFullDto(
            1L,
            1L,
            "username",
            "title",
            "content",
            LocalDateTime.now(),
            null,
            5,
            1L,
            1L,null
    );

    ReviewFullDto updatedReviewFullDto = new ReviewFullDto(
            1L,
            1L,
            "username",
            "title",
            "content",
            LocalDateTime.now(),
            null,
            5,
            1L,
            null,null
    );

    ReviewDto reviewDto = new ReviewDto(
            1L,
            "username",
            "title",
            "content",
            LocalDateTime.now(),
            null,
            5,
            1L
    );


    @Test
    @SneakyThrows
    void shouldAddLikeToReview() {
        Long reviewId = 1L;
        Long userId = 1L;
        Mockito.when(likeService.addLike(reviewId,userId)).thenReturn(savedReviewFullDto);

        mockMvc.perform(post("/reviews/1/like")
                        .header(X_REVIEW_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedReviewFullDto)));
    };




    @Test
    void shouldRemoveLikeToReview() {

    };



}