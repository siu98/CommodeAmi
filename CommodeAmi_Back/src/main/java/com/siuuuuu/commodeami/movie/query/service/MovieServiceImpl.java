package com.siuuuuu.commodeami.movie.query.service;

import com.siuuuuu.commodeami.common.exception.CommonException;
import com.siuuuuu.commodeami.common.exception.ErrorCode;
import com.siuuuuu.commodeami.movie.query.aggregate.Movie;
import com.siuuuuu.commodeami.movie.query.aggregate.MovieDTO;
import com.siuuuuu.commodeami.movie.query.repository.MovieMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            // 추후 errorcode 수정 예정
            throw new CommonException(ErrorCode.NOT_FOUND_REFRESH_TOKEN);
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
        movieDTO.setYoutubeUrl(movie.getYoutubeUrl());
        movieDTO.setCumulativeAudience(movie.getCumulativeAudience());
        movieDTO.setBoxOfficeRank(movie.getBoxOfficeRank());
        movieDTO.setApiId(movie.getApiId());

        return movieDTO;
    }

}
