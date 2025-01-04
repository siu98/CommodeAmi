package com.siuuuuu.commodeami.movie.command.aggregate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Table(name = "TBL_MOVIE")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="movie_id")
    private Long movieId;

    @Column(name="title")
    private String title;

    // 줄거리
    @Column(name="plot")
    private String plot;

    @Column(name="released_at")
    private Date releasedAt;

    @Column(name="poster_url")
    private String posterUrl;

    @Column(name="genre")
    private String genre;

    @Column(name="original_title")
    private String originalTitle;

    @Column(name="original_country")
    private String originalCountry;

    @Column(name="stills")
    private String stills;

    @Column(name="running_time")
    private Integer runningTime;

    @Column(name="trailers")
    private String trailers;

    @Column(name="youtube_url")
    private String youtubeUrl;

    @Column(name="cumulative_audience")
    private Long cumulativeAudience;

    @Column(name="box_office_rank")
    private Integer boxOfficeRank;

    @Column(name="api_id")
    private Long apiId;


    public Movie(Long movieId, String title, String plot, Date releasedAt) {
        this.movieId = movieId;
        this.title = title;
        this.plot = plot;
        this.releasedAt = releasedAt;
    }

}
