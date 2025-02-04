package com.siuuuuu.commodeami.movie.command.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import com.siuuuuu.commodeami.movie.command.domain.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class YoutubeAPIServiceImpl implements YoutubeAPIService {

    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate;

    @Value("${youtube.api.key}")
    private String youtubeApiKey;

    public YoutubeAPIServiceImpl(MovieRepository movieRepository,
                                 RestTemplate restTemplate) {
        this.movieRepository = movieRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<String> fetchYouTubeReviews(String query, int maxResults) {
        String youtubeUrl = "https://www.googleapis.com/youtube/v3/search" +
                "?part=snippet&q=" + query +
                "&type=video&videoCategoryId=1" + // "Film & Animation" 카테고리
                "&maxResults=" + maxResults +
                "&relevanceLanguage=ko" +
                "&key=" + youtubeApiKey;

        try {
            String response = restTemplate.getForObject(youtubeUrl, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            List<String> videoUrls = new ArrayList<>();

            for (JsonNode item: rootNode.path("items")) {
                String videoId = item.get("id").path("videoId").asText();
                if (!videoId.isEmpty()) {
                    videoUrls.add("https://www.youtube.com/watch?v=" + videoId);
                }
                if (videoUrls.size() >= maxResults) break;
            }

            log.info("'{}' 검색결과: {}", query, videoUrls);
            return videoUrls;

        } catch (Exception e) {
            log.error("❌ YouTube API 요청 실패: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void saveYouTubeReviews(Long startId, Long endId) {
        List<Movie> movies = movieRepository.findByMovieIdBetween(startId, endId);

        for (Movie movie : movies) {
            String query = movie.getTitle() +" 영화 리뷰";
            List<String> reviews = fetchYouTubeReviews(query, 5);

            if (!reviews.isEmpty()) {
                try {
                    // ✅ JSON 문자열로 변환하여 저장
                    ObjectMapper objectMapper = new ObjectMapper();
                    String youtubeUrlJson = objectMapper.writeValueAsString(reviews);

                    movie.setYoutubeUrl(youtubeUrlJson);
                    movieRepository.save(movie);
                    log.info("✅ '{}' (ID: {})의 YouTube 리뷰 URL 저장 완료: {}", movie.getTitle(), movie.getMovieId(), youtubeUrlJson);
                } catch (Exception e) {
                    log.error("❌ JSON 변환 실패: {}", e.getMessage());
                }
            } else {
                log.warn("⚠️ '{}'에 대한 YouTube 리뷰 영상 없음", movie.getTitle());
            }
        }
    }

    /**
     * ✅ 사용자가 직접 `startId`와 `endId`를 변경 후 실행할 수 있도록 설정
     * - 매일 자정(`cron = "0 0 0 * * *"`) 실행 (필요하면 직접 변경 가능)
     */
//    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 실행 (필요 시 변경)
    @Scheduled(fixedRate = 10000000)
    public void scheduledYouTubeReviewUpdate() {
        Long startId = 1L;  // ✅ 직접 설정 가능
        Long endId = 100L;    // ✅ 직접 설정 가능

        saveYouTubeReviews(startId, endId);
    }
}
