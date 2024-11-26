package ru.yandex.masterskaya.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.masterskaya.model.Like;
import ru.yandex.masterskaya.model.Review;
import ru.yandex.masterskaya.model.dto.ReviewStatisticDto;
import ru.yandex.masterskaya.model.dto.ReviewTopStatisticDto;
import ru.yandex.masterskaya.repository.LikeRepository;
import ru.yandex.masterskaya.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReviewStatisticServiceImplTest {
    private final ReviewStatisticService reviewStatisticService;
    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;


    @BeforeEach
    void setUp() {

        Review review1 = reviewRepository.save(
                Review.builder()
                        .authorId(1L)
                        .username("user1")
                        .title("Отличное мероприятие")
                        .content("Мне очень понравилось!")
                        .createdDateTime(LocalDateTime.now())
                        .updatedDateTime(LocalDateTime.now())
                        .mark(9)
                        .eventId(1L)
                        .build()
        );
        Review review2 = reviewRepository.save(
                Review.builder()
                        .authorId(1L)
                        .username("user2")
                        .title("Хорошо провели время")
                        .content("Было весело и интересно.")
                        .createdDateTime(LocalDateTime.now())
                        .updatedDateTime(LocalDateTime.now())
                        .mark(8)
                        .eventId(1L)
                        .build()
        );
        Review review3 = reviewRepository.save(
                Review.builder()
                        .authorId(1L)
                        .username("user3")
                        .title("Понравилось")
                        .content("Все было на уровне.")
                        .createdDateTime(LocalDateTime.now())
                        .updatedDateTime(LocalDateTime.now())
                        .mark(7)
                        .eventId(1L)
                        .build()
        );
        Review review4 = reviewRepository.save(
                Review.builder()
                        .authorId(1L)
                        .username("user4")
                        .title("Неплохо")
                        .content("В целом доволен.")
                        .createdDateTime(LocalDateTime.now())
                        .updatedDateTime(LocalDateTime.now())
                        .mark(6)
                        .eventId(1L)
                        .build()
        );
        Review review5 = reviewRepository.save(
                Review.builder()
                        .authorId(1L)
                        .username("user5")
                        .title("Средне")
                        .content("Ожидал большего.")
                        .createdDateTime(LocalDateTime.now())
                        .updatedDateTime(LocalDateTime.now())
                        .mark(5)
                        .eventId(1L)
                        .build()
        );


        List<Like> likes = List.of(
                Like.builder().review(review1).userId(1L).isLike(true).createdDateTime(LocalDateTime.now()).build(),
                Like.builder().review(review1).userId(2L).isLike(true).createdDateTime(LocalDateTime.now()).build(),
                Like.builder().review(review1).userId(3L).isLike(true).createdDateTime(LocalDateTime.now()).build(),
                Like.builder().review(review1).userId(4L).isLike(false).createdDateTime(LocalDateTime.now()).build(),
                Like.builder().review(review1).userId(5L).isLike(false).createdDateTime(LocalDateTime.now()).build(),
                Like.builder().review(review1).userId(6L).isLike(false).createdDateTime(LocalDateTime.now()).build(),
                Like.builder().review(review1).userId(7L).isLike(true).createdDateTime(LocalDateTime.now()).build(),
                Like.builder().review(review1).userId(8L).isLike(true).createdDateTime(LocalDateTime.now()).build(),
                Like.builder().review(review1).userId(9L).isLike(true).createdDateTime(LocalDateTime.now()).build(),
                Like.builder().review(review1).userId(10L).isLike(true).createdDateTime(LocalDateTime.now()).build(),
                Like.builder().review(review1).userId(10L).isLike(true).createdDateTime(LocalDateTime.now()).build()
        );

        likeRepository.saveAll(likes);
    }


    @Test
    void getStatistic() {
        ReviewStatisticDto statistic = reviewStatisticService.getStatisticByEvent(1L);
        assertNotNull(statistic);
        assertEquals(statistic.getAverageMark(), 9);
        assertEquals(statistic.getCountReview(), 5);
        assertEquals(statistic.getPercentGoodReview(), 80);
        assertEquals(statistic.getPercentBadReview(), 20);
    }

    @Test
    void getTopStatistic() {
        ReviewTopStatisticDto topStatistic = reviewStatisticService.getTopStatisticByEvent(1L);
        assertNotNull(topStatistic);
        assertEquals(topStatistic.getBestReview().size(), 3);
        assertEquals(topStatistic.getBadReview().size(), 1);
    }

    @Test
    void getStatisticByUser() {
        ReviewStatisticDto statisticByUser = reviewStatisticService.getStatisticByUser(1L);
        assertNotNull(statisticByUser);
        assertEquals(statisticByUser.getAverageMark(), 7.0);
        assertEquals(statisticByUser.getCountReview(), 5);
        assertEquals(statisticByUser.getPercentGoodReview(), 80);
        assertEquals(statisticByUser.getPercentBadReview(), 20);
    }
}