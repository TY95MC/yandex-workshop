package ru.yandex.masterskaya.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.yandex.masterskaya.model.Review;
import ru.yandex.masterskaya.model.TypeReview;
import ru.yandex.masterskaya.model.dto.ReviewStatisticDto;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {

    private final DataSource dataSource;

    @Override
    public ReviewStatisticDto getStatistic(Long eventId) {
        String sqlQuery = """
                SELECT (SELECT AVG(mark)
                        FROM (SELECT mark
                              FROM reviews
                                       LEFT JOIN review_likes rl ON reviews.review_id = rl.review_id
                              WHERE event_id = ?
                              GROUP BY reviews.review_id, mark
                              HAVING COUNT(*) > 10
                                 AND COUNT(CASE WHEN is_like = true THEN 1 END) >
                                     COUNT(CASE WHEN is_like = false THEN 1 END)) AS averageMark) AS averageMark,
                       COUNT(review_id)                                                           AS countReview,
                       (COUNT(CASE WHEN mark > 5 THEN 1 END) * 100 / COUNT(review_id))            AS percentGoodReview,
                       (COUNT(CASE WHEN mark <= 5 THEN 1 END) * 100 / COUNT(review_id))           AS percentBadReview
                FROM reviews
                WHERE event_id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setLong(1, eventId);
            preparedStatement.setLong(2, eventId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToReviewStatisticDto(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception: " + e.getMessage());
        }
        return null;
    }

    @Override
    public ReviewStatisticDto getStatisticByUser(Long authorId) {
        String sqlQuery = """
                SELECT (SELECT AVG(mark) FROM reviews where author_id = ?)        AS averageMark,
                       COUNT(review_id)                                           AS countReview,
                       (COUNT(CASE WHEN mark > 5 THEN 1 END) * 100.0 / COUNT(*))  AS percentGoodReview,
                       (COUNT(CASE WHEN mark <= 5 THEN 1 END) * 100.0 / COUNT(*)) AS percentBadReview
                FROM reviews
                WHERE event_id = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setLong(1, authorId);
            preparedStatement.setLong(2, authorId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToReviewStatisticDto(resultSet);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<List<Review>> getTopReview(Long eventId) {
        String sqlQuery = """
                (
                    SELECT 'BEST' AS review_type, r.*
                    FROM reviews r
                    WHERE event_id = ?
                    AND mark > 5
                    ORDER BY mark DESC
                    LIMIT 3
                )
                UNION ALL
                (
                    SELECT 'BAD' AS review_type, r.*
                    FROM reviews r
                    WHERE event_id = ?
                    AND mark <= 5
                    ORDER BY mark DESC
                    LIMIT 3
                );
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setLong(1, eventId);
            preparedStatement.setLong(2, eventId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Review> bestReviews = new ArrayList<>();
                List<Review> badReviews = new ArrayList<>();

                while (resultSet.next()) {
                    TypeReview reviewType = TypeReview.valueOf(resultSet.getString("review_type"));


                    Review review = mapToReview(resultSet);

                    switch (reviewType) {
                        case BEST -> bestReviews.add(review);
                        case BAD -> badReviews.add(review);
                    }
                }


                return List.of(bestReviews, badReviews);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private Review mapToReview(ResultSet resultSet) throws SQLException {
        Timestamp createdDateTime = resultSet.getTimestamp("created_date_time");
        Timestamp updatedDateTime = resultSet.getTimestamp("updated_date_time");

        return Review.builder()
                .id(resultSet.getLong("review_id"))
                .authorId(resultSet.getLong("author_id"))
                .username(resultSet.getString("username"))
                .title(resultSet.getString("title"))
                .content(resultSet.getString("content"))
                .createdDateTime(Objects.nonNull(createdDateTime) ? createdDateTime.toLocalDateTime() : null)
                .updatedDateTime(Objects.nonNull(updatedDateTime) ? updatedDateTime.toLocalDateTime() : null)
                .mark(resultSet.getInt("mark"))
                .eventId(resultSet.getLong("event_id"))
                .build();
    }


    private ReviewStatisticDto mapToReviewStatisticDto(ResultSet resultSet) throws SQLException {
        return ReviewStatisticDto.builder()
                .averageMark(resultSet.getInt("averageMark"))
                .countReview(resultSet.getInt("countReview"))
                .percentGoodReview(resultSet.getInt("percentGoodReview"))
                .percentBadReview(resultSet.getInt("percentBadReview"))
                .build();
    }
}
