package com.siuuuuu.commodeami.recommandation.command.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siuuuuu.commodeami.common.exception.CommonException;
import com.siuuuuu.commodeami.common.exception.ErrorCode;
import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import com.siuuuuu.commodeami.movie.command.domain.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final MovieRepository movieRepository;
    private final WeatherAPIService weatherAPIService;

    public RecommendationServiceImpl(MovieRepository movieRepository, WeatherAPIService weatherAPIService) {
        this.movieRepository = movieRepository;
        this.weatherAPIService = weatherAPIService;
    }

    @Override
    public List<Movie> recommendMovies(double latitude, double longitude) {
        String weatherCondition = weatherAPIService.getWeatherCondition(latitude, longitude);
        log.info("가져온 날씨 상태: {}", weatherCondition);

        if (weatherCondition == null) {
            log.error("날씨 정보를 가져오지 못했습니다.");
            throw new CommonException(ErrorCode.WEATHER_NOT_FOUND);
        }
// 비오는날 : 로맨스, 드라마, 음악  // 흐린날: 스릴러, 범죄, 공포, 미스터리    // 맑은날: 액션, SF, 모험, 다큐멘터리      // 눈: 애니메이션, 가족, 드라마, 판타지
        List<String> rainyGenres = Arrays.asList("로맨스", "드라마", "음악");
        List<String> snowyGenres = Arrays.asList("애니메이션", "가족", "드라마", "판타지");
        List<String> clearGenres = Arrays.asList("액션", "모험", "코미디", "SF", "다큐멘터리");
        List<String> cloudyGenres = Arrays.asList("스릴러", "범죄", "공포", "미스터리");

        List<Movie> filteredMovies;
//        if ("rain".equals(weatherCondition)) {
//            log.info("비 오는 날 추천 영화 필터링...");
//            filteredMovies = filterMoviesByGenre(rainyGenres, false);
//        } else if ("clouds".equals(weatherCondition)) {
//            log.info("구름 많은 날 추천 영화 필터링...");
//            filteredMovies = filterMoviesByGenre(cloudyGenres, false);
//        } else { // 맑은 날
//            log.info("맑은 날 추천 영화 필터링...");
//            filteredMovies = filterMoviesByGenre(clearGenres, true); // excludeRainy 적용
//        }
        if (weatherCondition.matches("(?i)rain|drizzle|thunderstorm")) {
            log.info("비 오는 날 추천 영화 필터링...");
            filteredMovies = filterMoviesByGenre(rainyGenres);
        } else if (weatherCondition.matches("(?i)snow")) {
            log.info("눈 오는 날 추천 영화 필터링...");
            filteredMovies = filterMoviesByGenre(snowyGenres);
        } else if (weatherCondition.matches("(?i)clouds|mist|haze|fog|smoke")) {
            log.info("흐린 날 추천 영화 필터링...");
            filteredMovies = filterMoviesByGenre(cloudyGenres);
        } else { // 맑은 날
            log.info("맑은 날 추천 영화 필터링...");
            filteredMovies = filterMoviesByGenre(clearGenres); // excludeRainy 적용 제거
        }


        log.info("최종 필터링된 영화 개수: {}", filteredMovies.size());

        if (filteredMovies.isEmpty()) {
//            log.warn("날씨 조건에 맞는 영화를 찾을 수 없습니다.");
            throw new CommonException(ErrorCode.MOVIE_NOT_FOUND_FOR_WEATHER);
        }

        Collections.shuffle(filteredMovies);
        return filteredMovies.subList(0, Math.min(10, filteredMovies.size()));
    }



    private List<Movie> filterMoviesByGenre(List<String> genrePool) {
        List<Movie> allMovies = movieRepository.findAll();

        log.info("전체 영화 개수: {}", allMovies.size());

        if (allMovies.isEmpty()) {
//            log.error("데이터베이스에 영화 데이터가 없습니다.");
            throw new CommonException(ErrorCode.MOVIE_NOT_FOUND);
        }

        List<Movie> filteredMovies = new ArrayList<>();

//        List<String> rainyGenres = Arrays.asList("스릴러", "공포", "범죄");

        for (Movie movie : allMovies) {
            try {
                List<String> movieGenres = Arrays.asList(movie.getGenre().split("\\s*,\\s*"));

                boolean matchesGenre = movieGenres.stream().anyMatch(genrePool::contains);

                if (matchesGenre) {
                    filteredMovies.add(movie);
                }
            } catch (Exception e) {
                log.warn("영화 장르 파싱 오류: {} - {}", movie.getTitle(), e.getMessage());
            }
        }
//                boolean isRainyGenre = movieGenres.stream().anyMatch(rainyGenres::contains); // 스릴러/공포/범죄 포함 여부 확인

//                if (excludeRainy) {
//                    if (matchesGenre && !isRainyGenre) { // clearGenres에 포함되면서, 스릴러/공포/범죄가 아닌 영화만 추가
////                        log.info("영화 '{}' 추가됨 (장르: {})", movie.getTitle(), movieGenres);
//                        filteredMovies.add(movie);
//                    }
//                } else {
//                    if (matchesGenre) { // 일반 필터링 로직
////                        log.info("영화 '{}' 추가됨 (장르: {})", movie.getTitle(), movieGenres);
//                        filteredMovies.add(movie);
//                    }
//                }
//            } catch (Exception e) {
//                log.warn("영화 장르 파싱 오류: {} - {}", movie.getTitle(), e.getMessage());
//            }
//        }

        log.info("최종 필터링된 영화 개수: {}", filteredMovies.size());
        return filteredMovies;
    }

}
