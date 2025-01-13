package com.siuuuuu.commodeami.movie.query.aggregate;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    @JsonProperty("movie_id")
    private Long movieId;

    @JsonProperty("title")
    private String title;

    // 줄거리
    @JsonProperty("plot")
    private String plot;

    @JsonProperty("released_at")
    private Date releasedAt;

    @JsonProperty("poster_url")
    private String posterUrl;

    @JsonProperty("genre")
    private String genre;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("original_country")
    private String originalCountry;

    @JsonProperty("stills")
    private String stills;

    @JsonProperty("running_time")
    private Integer runningTime;

    @JsonProperty("trailers")
    private String trailers;

    @JsonProperty("youtube_url")
    private String youtubeUrl;

    @JsonProperty("cumulative_audience")
    private Long cumulativeAudience;

    @JsonProperty("box_office_rank")
    private Integer boxOfficeRank;

    @JsonProperty("api_id")
    private Long apiId;
}
