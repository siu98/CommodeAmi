package com.siuuuuu.commodeami.movie.query.service;

import com.siuuuuu.commodeami.common.exception.CommonException;
import com.siuuuuu.commodeami.common.exception.ErrorCode;
import com.siuuuuu.commodeami.movie.query.aggregate.Movie;
import com.siuuuuu.commodeami.movie.query.aggregate.MovieDTO;
import com.siuuuuu.commodeami.movie.query.repository.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieMapper movieMapper;

    @Autowired
    public MovieServiceImpl(MovieMapper movieMapper) {
        this.movieMapper = movieMapper;
    }

    // 영화 전체 조회 구현
    @Override
    public List<Movie> findAllMovies() {
        return movieMapper.selectAllMovie();
    }

    @Override
    public MovieDTO findMovieById(Long movieId) {
        Movie movie = movieMapper.selectMovieById(movieId);

        if (movie == null) {
            throw new CommonException(ErrorCode.MOVIE_NOT_FOUND);
        }

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setMovieId(movie.getMovieId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setPlot(movie.getPlot());
        movieDTO.setReleasedAt(movie.getReleasedAt());
        movieDTO.setPosterUrl(movie.getPosterUrl());
        movieDTO.setGenre(movie.getGenre());
        movieDTO.setOriginalTitle(movie.getOriginalTitle());
        movieDTO.setOriginalCountry(movie.getOriginalCountry());
        movieDTO.setStills(movie.getStills());
        movieDTO.setRunningTime(movie.getRunningTime());
        movieDTO.setTrailers(movie.getTrailers());
        movieDTO.setYoutubeUrl(movie.getYoutubeUrl());
        movieDTO.setCumulativeAudience(movie.getCumulativeAudience());
        movieDTO.setBoxOfficeRank(movie.getBoxOfficeRank());
        movieDTO.setApiId(movie.getApiId());

        return movieDTO;
    }

    @Override
    public List<MovieDTO> findMovieByTitle(String title) {
        List<Movie> movies = movieMapper.selectMovieByTitle(title);

    // 검색 결과가 없을 때 빈 리스트 반환
        if (movies == null || movies.isEmpty()) {
            return Collections.emptyList();
        }


        return movies.stream().map(movie -> {
            MovieDTO movieDTO = new MovieDTO();
            movieDTO.setMovieId(movie.getMovieId());
            movieDTO.setTitle(movie.getTitle());
            movieDTO.setPlot(movie.getPlot());
            movieDTO.setReleasedAt(movie.getReleasedAt());
            movieDTO.setPosterUrl(movie.getPosterUrl());
            movieDTO.setGenre(movie.getGenre());
            movieDTO.setOriginalTitle(movie.getOriginalTitle());
            movieDTO.setOriginalCountry(movie.getOriginalCountry());
            movieDTO.setStills(movie.getStills());
            movieDTO.setRunningTime(movie.getRunningTime());
            movieDTO.setTrailers(movie.getTrailers());
            movieDTO.setYoutubeUrl(movie.getYoutubeUrl());
            movieDTO.setCumulativeAudience(movie.getCumulativeAudience());
            movieDTO.setBoxOfficeRank(movie.getBoxOfficeRank());
            movieDTO.setApiId(movie.getApiId());
            return movieDTO;
        }).collect(Collectors.toList()); // Java 16 미만에서 사용
    }
}
