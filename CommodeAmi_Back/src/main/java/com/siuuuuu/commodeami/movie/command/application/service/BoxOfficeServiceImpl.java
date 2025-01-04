package com.siuuuuu.commodeami.movie.command.application.service;

import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import com.siuuuuu.commodeami.movie.command.domain.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class BoxOfficeServiceImpl implements BoxOfficeService {
    @Value("${kobis.api.url}")
    private String kobisApiUrl;

    @Value("${kobis.api.key}")
    private String kobisApiKey;

    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;

    public BoxOfficeServiceImpl(RestTemplate restTemplate,
                                MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?") // 매일 오전 1시에 실행
    @Scheduled(fixedRate = 10000000)
    @Transactional
    public void updateBoxOfficeData() {

        log.info("Fetching daily box office data from KOBIS API...");

//        String targetDate = "20250104"; // 예시 날짜, 동적으로 설정 가능

        // 오늘 날짜에서 하루 전날짜를 생성 (KOBIS API는 과거 날짜의 데이터를 반환)
        String targetDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String url = String.format("%s?key=%s&targetDt=%s", kobisApiUrl, kobisApiKey, targetDate);

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response != null) {
                Map<String, Object> boxOfficeResult = (Map<String, Object>) response.get("boxOfficeResult");
                List<Map<String, Object>> dailyBoxOfficeList = (List<Map<String, Object>>) boxOfficeResult.get("dailyBoxOfficeList");

                for (Map<String, Object> entry : dailyBoxOfficeList) {
                    String title = ((String) entry.get("movieNm")).toLowerCase().trim();
                    Optional<Movie> optionalMovie = movieRepository.findByTitleIgnoreCase(title);

                    if (optionalMovie.isPresent()) {
                        Movie movie = optionalMovie.get();

                        // 박스오피스 데이터 업데이트
                        movie.setBoxOfficeRank(Integer.parseInt((String) entry.get("rank")));
                        movie.setCumulativeAudience(Long.parseLong((String) entry.get("audiAcc")));
                        movieRepository.save(movie);

                        log.info("Updated movie: {} with rank: {} and audience: {}", title, movie.getBoxOfficeRank(), movie.getCumulativeAudience());
                    } else {
                        log.warn("Movie '{}' not found in database", title);
                    }
                }
            } else {
                log.warn("No data received from KOBIS API");
            }
        } catch (Exception e) {
            log.error("Error fetching or updating box office data: {}", e.getMessage(), e);
        }
    }
}
