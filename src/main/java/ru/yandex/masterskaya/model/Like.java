package ru.yandex.masterskaya.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static ru.yandex.masterskaya.constants.Constants.DATE_TIME_FORMAT;

@Entity
@Table(name = "review_likes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean isLike;

    @Column(name = "created_datetime", updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime createdDateTime;

    @PrePersist
    protected void onCreate() {
        this.createdDateTime = LocalDateTime.now();
    }
}
