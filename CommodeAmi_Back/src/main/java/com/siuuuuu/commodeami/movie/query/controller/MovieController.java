package com.siuuuuu.commodeami.movie.query.controller;

import com.siuuuuu.commodeami.common.ResponseDTO;
import com.siuuuuu.commodeami.movie.query.aggregate.Movie;
import com.siuuuuu.commodeami.movie.query.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
    // 영화 전체 조회
    @GetMapping("")
    public ResponseDTO<?> findAllMovies() {
        List<Movie> movies = movieService.findAllMovies();
//        System.out.println("영화 전체 데이터: " + movies);
        return ResponseDTO.ok(movies);
    }

    // 영화 id로 조회
    @GetMapping("/{movieId}")
    public ResponseDTO<?> findMovieById(@PathVariable("movieId") Long movieId) {
        return ResponseDTO.ok(movieService.findMovieById(movieId));
    }

    // 영화 제목으로 조회 (검색)
    @GetMapping("/search")
    public ResponseDTO<?> findMovieByName(@RequestParam String title) {
        return ResponseDTO.ok(movieService.findMovieByTitle(title));
    }
}
