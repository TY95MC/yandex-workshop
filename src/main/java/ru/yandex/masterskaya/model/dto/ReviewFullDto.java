package ru.yandex.masterskaya.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static ru.yandex.masterskaya.constants.Constants.DATE_TIME_FORMAT;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewFullDto {
    private Long id;
    private Long authorId;
    private String username;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime createdDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private LocalDateTime updatedDateTime;
    private Integer mark;
    private Long eventId;
    private Long numberOfLikes;
    private Long numberOfDisLikes;


    @Override
    public String toString() {
        return "ReviewFullDto{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdDateTime=" + createdDateTime +
                ", updatedDateTime=" + updatedDateTime +
                ", mark=" + mark +
                ", eventId=" + eventId +
                ", numberOfLikes=" + numberOfLikes +
                ", numberOfDisLikes=" + numberOfDisLikes +
                '}';
    }
}
