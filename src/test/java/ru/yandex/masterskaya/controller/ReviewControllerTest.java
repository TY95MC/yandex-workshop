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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.yandex.masterskaya.exception.EntityNotFoundException;
import ru.yandex.masterskaya.model.dto.CreateReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.model.dto.UpdateReviewDto;
import ru.yandex.masterskaya.service.LikeService;
import ru.yandex.masterskaya.service.ReviewService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.yandex.masterskaya.constants.Constants.X_REVIEW_USER_ID;

@WebMvcTest(controllers = ReviewController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ReviewControllerTest {

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
            null,null
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
    void shouldAddReviewSuccessfully() throws Exception {
        Mockito
                .when(service.createReview(any(CreateReviewDto.class)))
                .thenReturn(savedReviewFullDto);

        mockMvc.perform(post("/reviews")
                        .content(objectMapper.writeValueAsString(createReviewDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedReviewFullDto)));
    }

    @Test
    void shouldFailCreateInvalidReview() throws Exception {
        Mockito
                .when(service.createReview(any(CreateReviewDto.class)))
                .thenReturn(savedReviewFullDto);

        //ревью с невалидными полями
        mockMvc.perform(post("/reviews")
                        .content(objectMapper.writeValueAsString(createFailDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void shouldUpdateReviewSuccessfully() throws Exception {
        Mockito
                .when(service.updateReview(anyLong(), anyLong(), any(UpdateReviewDto.class)))
                .thenReturn(updatedReviewFullDto);

        mockMvc.perform(patch("/reviews/{id}", savedReviewFullDto.getId())
                        .content(objectMapper.writeValueAsString(updateReviewDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_REVIEW_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedReviewFullDto)));
    }

    @Test
    void shouldFailUpdateReview() throws Exception {
        Mockito
                .when(service.updateReview(anyLong(), anyLong(), any(UpdateReviewDto.class)))
                .thenReturn(updatedReviewFullDto);

        //апдейт ревью с невалидными полями
        mockMvc.perform(patch("/reviews/{id}", savedReviewFullDto.getId())
                        .content(objectMapper.writeValueAsString(updateFailReviewDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_REVIEW_USER_ID, 1))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void shouldGetReviewSuccessfully() throws Exception {
        Mockito
                .when(service.getReview(anyLong()))
                .thenReturn(reviewDto);

        mockMvc.perform(get("/reviews/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(reviewDto)));
    }

    @Test
    void shouldFailGetReview() throws Exception {
        Mockito
                .when(service.getReview(anyLong()))
                .thenThrow(new EntityNotFoundException("Review with id=" + anyLong() + " was not found"));

        mockMvc.perform(get("/reviews/{id}", savedReviewFullDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldGetReviewsSuccessfully() throws Exception {
        Pageable page = PageRequest.of(1, 1, Sort.by("id").ascending());
        Slice<ReviewDto> slice = new SliceImpl<>(List.of(reviewDto), page, false);

        Mockito
                .when(service.getReviews(anyInt(), anyInt(), anyLong()))
                .thenReturn(slice);

        mockMvc.perform(get("/reviews?page={page}&size={size}&eventId={eventId}", 1, 2, 3)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(slice)));
    }

    @Test
    void shouldFailGetReviews() throws Exception {
        Pageable page = PageRequest.of(1, 1, Sort.by("id").ascending());
        Slice<ReviewDto> slice = new SliceImpl<>(List.of(reviewDto), page, false);

        Mockito
                .when(service.getReviews(anyInt(), anyInt(), anyLong()))
                .thenReturn(slice);

        //невалидные параметры пути запроса
        mockMvc.perform(get("/reviews?page={page}&size={size}&eventId={eventId}", -1, 200, 300)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is5xxServerError());
    }

    @Test
    void shouldDeleteReviewsSuccessfully() throws Exception {
        Mockito
                .doNothing()
                .when(service).delete(anyLong(), anyLong());

        mockMvc.perform(delete("/reviews/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_REVIEW_USER_ID, "1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void shouldFailDeleteReviews() throws Exception {
        Mockito
                .doNothing()
                .when(service).delete(anyLong(), anyLong());

        //некорректный ревью айди
        mockMvc.perform(delete("/reviews/{id}", -11)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_REVIEW_USER_ID, "1"))
                .andExpect(status().is5xxServerError());

        //некорректный айди автора
        mockMvc.perform(delete("/reviews/{id}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(X_REVIEW_USER_ID, "-11"))
                .andExpect(status().is5xxServerError());
    }


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

}