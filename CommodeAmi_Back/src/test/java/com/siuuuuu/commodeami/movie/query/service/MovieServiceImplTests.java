package com.siuuuuu.commodeami.movie.query.service;

import com.siuuuuu.commodeami.movie.query.aggregate.Movie;
import com.siuuuuu.commodeami.movie.query.aggregate.MovieDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MovieServiceImplTests {

    @Autowired
    private MovieService movieService;

    @DisplayName("영화 전체 조회")
    @Test
    void testGetAllMovies() {
        // given

        // when
        List<Movie> movieList = movieService.findAllMovies();

        // then
        assertNotNull(movieList, "영화 목록이 null이 아닙니다.");
        assertFalse(movieList.isEmpty(), "영화 목록이 비어있지 않아야 합니다.");
        movieList.forEach(movie -> {
            log.info("movie: {}", movie);
        });
    }

    @DisplayName("영화 id로 조회")
    @Test
    void testGetMovieById() {
        // given: 테스트할 movieId (DB에 해당 ID가 존재해야 함)
        Long movieId = 1L;

        // when
        MovieDTO movieDTO = movieService.findMovieById(movieId);

        // then
        assertNotNull(movieDTO, "영화 정보가 null이 아닙니다.");
        assertEquals(movieId, movieDTO.getMovieId(), "조회한 영화 ID가 일치해야 합니다.");

        log.info("영화 정보: {}", movieDTO);
    }

    @DisplayName("영화 제목으로 조회(검색)")
    @Test
    void testGetMovieByName() {
        // given: 테스트할 영화 제목 입력
        String title = "해리";

        // when
        List<MovieDTO> movieDTOList = movieService.findMovieByTitle(title);

        // then
        assertNotNull(movieDTOList, "영화 목록이 null이 아닙니다.");
        assertFalse(movieDTOList.isEmpty(), "영화 목록이 비어있지 않아야 합니다.");

        movieDTOList.forEach(movie -> {
            assertTrue(movie.getTitle().contains(title),
                    "검색된 영화 제목에 '" + title + "'이 포함되어 있어야 합니다. (실제 제목: " + movie.getTitle() + ")");
            log.info("검색된 영화 정보: {}", movie);
        });
    }
}