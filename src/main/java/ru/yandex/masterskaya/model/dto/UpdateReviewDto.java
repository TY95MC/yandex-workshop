package ru.yandex.masterskaya.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateReviewDto {
    private String username;
    @Length(max = 100)
    private String title;
    @Length(max = 1500)
    private String content;
    @Min(1)
    @Max(10)
    private Integer mark;
}
