package ru.yandex.masterskaya.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.masterskaya.model.dto.CreateReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.model.dto.UpdateReviewDto;
import ru.yandex.masterskaya.service.ReviewService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private final ReviewService service;


    CreateReviewDto createDto = new CreateReviewDto(
            1L,
            "username",
            "title",
            "content",
            5,
            1L
    );

    UpdateReviewDto updateReviewDto = new UpdateReviewDto(
            "username",
            "new title",
            "new content",
            3
    );

    ReviewFullDto savedReview = new ReviewFullDto(
            1L,
            1L,
            "username",
            "title",
            "content",
            LocalDateTime.now(),
            null,
            5,
            1L
    );

    ReviewFullDto updatedReview = new ReviewFullDto(
            1L,
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
                .thenReturn(savedReview);

        mockMvc.perform(post("/reviews")
                        .content(objectMapper.writeValueAsString(createDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(savedReview)));
    }

    @Test
    void update() {
    }

    @Test
    void get() {
    }

    @Test
    void getRevs() {
    }

    @Test
    void delete() {
    }
}