package ru.yandex.masterskaya.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.masterskaya.model.dto.ReviewStatisticDto;
import ru.yandex.masterskaya.model.dto.ReviewTopStatisticDto;
import ru.yandex.masterskaya.service.ReviewStatisticService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@WebMvcTest(controllers = ReviewStatisticController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
class ReviewStatisticControllerTest {
    @MockBean
    private ReviewStatisticService reviewStatisticService;


    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    ReviewStatisticDto reviewStatisticDto = ReviewStatisticDto.builder()
            .averageMark(7.0)
            .countReview(20)
            .percentGoodReview(50)
            .percentBadReview(50)
            .build();

    ReviewTopStatisticDto reviewTopStatisticDto = ReviewTopStatisticDto.builder()
            .bestReview(Collections.emptyList())
            .badReview(Collections.emptyList())
            .build();

    @Test
    @SneakyThrows
    void getStatistic() {
        Mockito.when(reviewStatisticService.getStatisticByEvent(Mockito.anyLong()))
                .thenReturn(reviewStatisticDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/statistic/{eventId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewStatisticDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    void getTopStatistic() {
        Mockito.when(reviewStatisticService.getStatisticByUser(Mockito.anyLong())).thenReturn(reviewStatisticDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/statistic/top/{eventId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewTopStatisticDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());


    }

    @Test
    @SneakyThrows
    void getStatisticByAuthor() {
        mockMvc.perform(MockMvcRequestBuilders.get("/statistic/{eventId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewStatisticDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}