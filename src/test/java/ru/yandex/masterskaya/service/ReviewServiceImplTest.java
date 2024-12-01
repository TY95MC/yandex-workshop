package ru.yandex.masterskaya.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.masterskaya.exception.ConflictException;
import ru.yandex.masterskaya.exception.EntityNotFoundException;
import ru.yandex.masterskaya.model.dto.CreateReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewDto;
import ru.yandex.masterskaya.model.dto.ReviewFullDto;
import ru.yandex.masterskaya.model.dto.UpdateReviewDto;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
@Transactional
class ReviewServiceImplTest {

    private final ReviewService service;

    CreateReviewDto createReviewDto = new CreateReviewDto(
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

    @AfterAll
    static void cleanDB() {
        try {
            File directory = new File("db-tests");
            deleteDirectory(directory);
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }

    @Test
    void shouldCreateReviewSuccessfully() {
        ReviewFullDto answer = service.createReview(createReviewDto);
        assertThat(answer.getContent(), equalTo(createReviewDto.getContent()));
    }

    @Test
    void shouldUpdateReviewSuccessfully() {
        ReviewFullDto answer = service.createReview(createReviewDto);
        assertThat(answer.getContent(), equalTo(createReviewDto.getContent()));

        answer = service.updateReview(answer.getAuthorId(), answer.getId(), updateReviewDto);
        assertThat(answer.getContent(), equalTo(updateReviewDto.getContent()));
    }

    @Test
    void shouldFailUpdateReview() {
        ReviewFullDto answer = service.createReview(createReviewDto);
        assertThat(answer.getContent(), equalTo(createReviewDto.getContent()));

        Long wrongAuthorId = 10L;
        ConflictException thrown = Assertions.assertThrows(ConflictException.class,
                () -> service.updateReview(wrongAuthorId, answer.getId(), updateReviewDto));

        assertThat(thrown.getMessage(),
                equalTo("User with id=" + wrongAuthorId + "is not review author"));
    }

    @Test
    void shouldGetReviewSuccessfully() {
        ReviewFullDto answer1 = service.createReview(createReviewDto);

        ReviewDto answer2 = service.getReview(answer1.getId());
        assertThat(answer1.getTitle(), equalTo(answer2.getTitle()));
        assertThat(answer1.getContent(), equalTo(answer2.getContent()));
        assertThat(answer1.getEventId(), equalTo(answer2.getEventId()));
    }

//    @Test
//    void shouldGetReviewsSuccessfully() {
//        service.createReview(createReviewDto);
//        service.createReview(createReviewDto);
//        service.createReview(createReviewDto);
//
//        Slice<ReviewDto> result = service.getReviews(0, 5, createReviewDto.getEventId());
//        assertThat(result.getContent().size(), equalTo(3));
//    }

    @Test
    void shouldDeleteReviewSuccessfully() {
        ReviewFullDto answer1 = service.createReview(createReviewDto);
        ReviewDto answer2 = service.getReview(answer1.getId());

        assertThat(answer1.getId(), equalTo(answer2.getId()));

        service.delete(answer1.getAuthorId(), answer1.getId());

        EntityNotFoundException thrown =
                Assertions.assertThrows(EntityNotFoundException.class, () -> service.getReview(answer1.getId()));

        assertThat(thrown.getMessage(),
                equalTo("Review with id=" + answer1.getId() + " was not found"));
    }
}