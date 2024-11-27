package ru.yandex.masterskaya.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.masterskaya.model.Like;

import java.util.Optional;
import java.util.Set;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByReviewIdAndUserId(Long reviewId, Long userId);

    Optional<Set<Like>> findByReviewId(Long reviewId);

    void deleteByReviewIdAndUserId(Long reviewId, Long userId);
}
