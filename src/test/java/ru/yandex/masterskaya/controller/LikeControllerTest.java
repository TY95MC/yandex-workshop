package ru.yandex.masterskaya.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.service.LikeService;

import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.masterskaya.constants.Constants.X_REVIEW_USER_ID;


@WebMvcTest(controllers = LikeController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikeControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private final LikeService likeService;


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
            1L,0L
    );

    ReviewFullDto savedReviewFullDto2 = new ReviewFullDto(
            1L,
            1L,
            "username",
            "title",
            "content",
            LocalDateTime.now(),
            null,
            5,
            1L,
            1L,1L
    );



    @Test
    @SneakyThrows
    void shouldAddLikeToReview() {
        Long reviewId = 1L;
        Long userId = 2L;

        when(likeService.addLike(eq(reviewId),eq(userId))).thenReturn(savedReviewFullDto);

        mockMvc.perform(post("/reviews/{id}/like", reviewId)
                        .header(X_REVIEW_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedReviewFullDto)));
    };


    @Test
    @SneakyThrows
    void shouldRemoveLikeFromReview() {
        Long reviewId = 1L;
        Long userId = 2L;
        when(likeService.removeLike(reviewId,userId)).thenReturn(savedReviewFullDto);

        mockMvc.perform(delete("/reviews/1/like")
                        .header(X_REVIEW_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedReviewFullDto)));
    };

    @Test
    @SneakyThrows
    void shouldAddDisLikeToReview() {
        Long reviewId = 1L;
        Long userId = 2L;
        when(likeService.addDislike(reviewId,userId)).thenReturn(savedReviewFullDto2);

        mockMvc.perform(post("/reviews/1/dislike")
                        .header(X_REVIEW_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedReviewFullDto2)));
    };

    @Test
    @SneakyThrows
    void shouldRemoveDisLikeFromReview() {
        Long reviewId = 1L;
        Long userId = 2L;
        when(likeService.removeDislike(reviewId,userId)).thenReturn(savedReviewFullDto2);

        mockMvc.perform(delete("/reviews/1/dislike")
                        .header(X_REVIEW_USER_ID, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedReviewFullDto2)));
    };
}