package com.toyproject.movie.core.domain.movie;

import com.toyproject.movie.core.domain.base.DefaultTimeStampCreatedAndModifiedEntity;
import com.toyproject.movie.global.enums.AudienceType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "movie")
@Entity
public class Movie extends DefaultTimeStampCreatedAndModifiedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_idx", columnDefinition = "BIGINT UNSIGNED")
    private Long movieIdx;

    @Column(name = "movie_title", nullable = false, length = 50)
    private String movieTitle;

    @Column(name = "runtime", nullable = false, columnDefinition = "TIME WITHOUT TIME ZONE")
    private LocalTime runtime;

    @Column(name = "audience_type", nullable = false,
            comment = "0: 전체 관람가, 12: 12세이상관람가, 15: 15세이상관람가, 18: 18세이상관람가")
    @Convert(converter = AudienceType.Converter.class)
    private AudienceType audienceType;

    @Column(name = "total_audience", nullable = false)
    private Integer totalAudience = 0;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    private Movie(String movieTitle, LocalTime runtime, AudienceType audienceType, Integer totalAudience, LocalDate releaseDate, Boolean isDeleted) {
        this.movieTitle = movieTitle;
        this.runtime = runtime;
        this.audienceType = audienceType;
        this.totalAudience = totalAudience;
        this.releaseDate = releaseDate;
        this.isDeleted = isDeleted;
    }

    public static Movie of(String movieTitle, LocalTime runtime, AudienceType audienceType, Integer totalAudience, LocalDate releaseDate, Boolean isDeleted) {
        return new Movie(movieTitle, runtime, audienceType, totalAudience, releaseDate, isDeleted);
    }
}
