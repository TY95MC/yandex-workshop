package ru.yandex.masterskaya.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.masterskaya.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
    @Query("SELECT r " +
            "FROM Review r " +
            "WHERE r.eventId = ?1")
    Slice<Review> findReviews(Long eventId, Pageable page);
}
