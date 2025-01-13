package com.siuuuuu.commodeami.movie.query.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    private Long movieId;
    private String title;
    private String plot;
    private Date releasedAt;
    private String posterUrl;
    private String genre;
    private String originalTitle;
    private String originalCountry;
    private String stills;
    private Integer runningTime;
    private String trailers;
    private String youtubeUrl;
    private Long cumulativeAudience;
    private Integer boxOfficeRank;
    private Long apiId;
}
