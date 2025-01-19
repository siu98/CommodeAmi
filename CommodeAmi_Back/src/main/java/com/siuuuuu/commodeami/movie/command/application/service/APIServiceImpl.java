package com.siuuuuu.commodeami.movie.command.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siuuuuu.commodeami.actor.command.application.service.AppActorService;
import com.siuuuuu.commodeami.actor.command.domain.repository.ActorRepository;
import com.siuuuuu.commodeami.movie.command.aggregate.dto.GenreDTO;
import com.siuuuuu.commodeami.movie.command.aggregate.dto.MovieDetailDTO;
import com.siuuuuu.commodeami.movie.command.aggregate.dto.PopularMovieDTO;
import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import com.siuuuuu.commodeami.movie.command.domain.repository.MovieRepository;
import com.siuuuuu.commodeami.movieactor.command.domain.repository.MovieActorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class APIServiceImpl implements APIService {

    @Value("${tmdb.api.url}")
    private String tmdbApiUrl;

    @Value("${tmdb.api.key}")
    private String tmdbApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final MovieActorRepository movieActorRepository;
    private final MovieCastService movieCastService;

//    @Autowired
    private final AppActorService appActorService;


    public APIServiceImpl(MovieRepository movieRepository,
                          ActorRepository actorRepository,
                          MovieActorRepository movieActorRepository,
                          MovieCastService movieCastService,
                          AppActorService appActorService) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.movieActorRepository = movieActorRepository;
        this.movieCastService = movieCastService;
        this.appActorService = appActorService;
    }

//    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(fixedRate = 10000000)
    @Override
//    @Transactional
    public List<PopularMovieDTO> fetchPopularMovies() {
        log.info("Fetching popular movies from TMDB API...");
        int totalPagesToFetch = 500; // 가져올 페이지 수 설정 (필요에 따라 조정 가능)
        List<PopularMovieDTO> allPopularMovies = new ArrayList<>();

        try {
            for (int page = 1; page <= totalPagesToFetch; page++) {
                String url = String.format("%s/popular?api_key=%s&language=ko-KR&page=%d", tmdbApiUrl, tmdbApiKey, page);
                log.info("Requesting TMDB API (Page {}): {}", page, url);

                Map<String, Object> response = restTemplate.getForObject(url, Map.class);

                if (response != null && response.containsKey("results")) {
                    List<?> results = (List<?>) response.get("results");
                    log.info("Fetched {} movies from TMDB (Page {})", results.size(), page);

                    // API에서 받은 데이터를 DTO로 변환
                    List<PopularMovieDTO> popularMovies = results.stream()
                            .map(result -> {
                                try {
                                    return objectMapper.convertValue(result, PopularMovieDTO.class);
                                } catch (Exception e) {
                                    log.error("Failed to map result: {}", result, e);
                                    return null;
                                }
                            })
                            .filter(dto -> dto != null)
                            .collect(Collectors.toList());

                    allPopularMovies.addAll(popularMovies);

                    // DB에 저장 또는 업데이트 (api_id 기준으로 매칭)
                    for (PopularMovieDTO dto : popularMovies) {
                        movieRepository.findByApiId(dto.getId()).ifPresentOrElse(
                                existingMovie -> log.info("Movie already exists: {}", existingMovie.getTitle()),
                                () -> {
                                    Movie newMovie = new Movie();
                                    newMovie.setApiId(dto.getId()); // API의 id를 별도 필드에 저장
                                    newMovie.setTitle(dto.getTitle());
                                    newMovie.setReleasedAt(dto.getReleased_at());
                                    newMovie.setPlot(dto.getPlot());
                                    movieRepository.save(newMovie);

                                    // 상세 정보 가져오기
//                                    fetchMovieDetails(dto.getId());
                                    updateMovieStills(dto.getId());
                                    updateMovieTrailers(dto.getId());
                                    // 상세 정보 가져오기
                                    MovieDetailDTO movieDetail = fetchMovieDetails(dto.getId());
                                    if (movieDetail != null) {

                                        // 영화-배우 관계 업데이트
                                        updateMovieCast(dto.getId(), newMovie);
                                    }
                                }
                        );
                    }
                } else {
                    log.warn("No results found for page {}", page);
                    break; // 더 이상 데이터가 없으면 반복 종료
                }
            }
        } catch (Exception e) {
            log.error("Error fetching popular movies: {}", e.getMessage(), e);
        }
        log.info("영화 업데이트 종료");
        return allPopularMovies;
    }


    @Override
    @Transactional
    public MovieDetailDTO fetchMovieDetails(Long apiId) {
        log.info("Fetching movie details for apiId: {}", apiId);

        // DB에서 특정 영화 조회
        Movie movie = movieRepository.findByApiId(apiId)
                .orElseThrow(() -> new IllegalArgumentException("Movie with apiId " + apiId + " not found"));

        String url = String.format("%s/%d?api_key=%s&language=ko-KR", tmdbApiUrl, apiId, tmdbApiKey);
        log.info("Requesting details for movie: {}, URL: {}", movie.getTitle(), url);

        try {
            // TMDB API 요청
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            log.info("API Response for {}: {}", movie.getTitle(), response);

            if (response != null) {
                // API 응답을 DTO로 매핑
                MovieDetailDTO movieDetail = objectMapper.convertValue(response, MovieDetailDTO.class);

                // Poster URL 생성
                String posterPath = (String) response.get("poster_path"); // API 응답에서 poster_path 추출
                String posterUrl = null;
                if (posterPath != null) {
                    posterUrl = "https://image.tmdb.org/t/p/original" + posterPath;
                }

                // origin_country 추출
                List<String> originCountries = (List<String>) response.get("origin_country");
                String originCountry = originCountries != null ? String.join(", ", originCountries) : null;


                // 엔티티 업데이트
                movie.setRunningTime(movieDetail.getRuntime());
                movie.setGenre(movieDetail.getGenres().stream()
                        .map(GenreDTO::getName) // Genre 내부 필드에 접근
                        .collect(Collectors.joining(", ")));
                movie.setPosterUrl(posterUrl);
                movie.setOriginalCountry(originCountry);
                movie.setOriginalTitle(movieDetail.getOriginal_title());
                movieRepository.save(movie);

                log.info("Updated movie: {}", movie);


                // 요청한 영화의 세부 정보를 반환
                return movieDetail;
            }
        } catch (Exception e) {
            log.error("Error fetching details for movie {}: {}", movie.getTitle(), e.getMessage(), e);
        }

        // API 요청 실패 시 null 반환 (필요에 따라 Optional로 감싸는 것도 고려)
        return null;
    }

// updateMovieActorRelationship 메서드 추가
//    @Transactional
//    public void updateMovieActorRelationship(Movie movie, Actor actor) {
//        Optional<MovieActor> existingRelationship = movieActorRepository.findByMovieIdAndActorId(movie.getMovieId(), actor.getActorId());
//
//        if (existingRelationship.isEmpty()) {
//            MovieActor movieActor = new MovieActor();
//            movieActor.setMovie(movie);
//            movieActor.setActor(actor);
//            movieActorRepository.save(movieActor);
//        } else {
//            log.info("Relationship already exists for movie {} and actor {}", movie.getMovieId(), actor.getActorId());
//        }
//    }

    @Override
    @Transactional
    public void updateMovieCast(Long apiId, Movie movie) {
        String creditsUrl = String.format("%s/%d/credits?api_key=%s&language=ko-KR", tmdbApiUrl, apiId, tmdbApiKey);
        log.info("Requesting credits for movie: {}, URL: {}", movie.getTitle(), creditsUrl);

        try {
            Map<String, Object> creditsResponse = restTemplate.getForObject(creditsUrl, Map.class);
            if (creditsResponse == null || !creditsResponse.containsKey("cast")) {
                log.warn("No cast information found for movie: {}", movie.getTitle());
                return;
            }

            List<?> cast = (List<?>) creditsResponse.get("cast");
            log.info("Fetched {} cast members for movie: {}", cast.size(), movie.getTitle());

            // Use MovieCastService to process the cast
            movieCastService.updateMovieCast(movie, cast);

        } catch (Exception e) {
            log.error("Error updating cast for movie {}: {}", movie.getTitle(), e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void updateMovieStills(Long apiId) {

        // DB에서 특정 영화 조회
        Movie movie = movieRepository.findByApiId(apiId)
                .orElseThrow(() -> new IllegalArgumentException("Movie with apiId " + apiId + " not found"));

        String stillsUrl = String.format("%s/%d/images?api_key=%s&", tmdbApiUrl, apiId, tmdbApiKey);
        log.info("Requesting stills for movie: {}, URL: {}", movie.getTitle(), stillsUrl);

        try {
            // TMDB API 요청
            Map<String, Object> stillsResponse = restTemplate.getForObject(stillsUrl, Map.class);
            log.info("API Response for {}: {}", movie.getTitle(), stillsResponse);

            if (stillsResponse != null && stillsResponse.containsKey("backdrops")) {
                List<Map<String, Object>> backdrops = (List<Map<String, Object>>) stillsResponse.get("backdrops");

                List<String> stillsList = new ArrayList<>();
                for (Map<String, Object> backdrop : backdrops) {
                    String filePath = (String) backdrop.get("file_path"); // 이미지 경로
                    if (filePath != null) {
                        stillsList.add(filePath);
                    }
                }

                // JSON 형식으로 변환하여 저장
                String stillsJson = objectMapper.writeValueAsString(stillsList);
                movie.setStills(stillsJson);

                // 업데이트 저장
                movieRepository.save(movie);
                log.info("Updated stills for movie: {}", movie.getTitle());
            } else {
                log.warn("No stills found for movie: {}", movie.getTitle());
            }

        } catch (Exception e) {
            log.error("Error fetching stills for movie {}: {}", movie.getTitle(), e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void updateMovieTrailers(Long apiId) {
        // DB에서 특정 영화 조회
        Movie movie = movieRepository.findByApiId(apiId)
                .orElseThrow(() -> new IllegalArgumentException("Movie with apiId " + apiId + " not found"));

        String videosUrl = String.format("%s/%d/videos?api_key=%s&language=ko-KR", tmdbApiUrl, apiId, tmdbApiKey);
        log.info("Requesting trailers for movie: {}, URL: {}", movie.getTitle(), videosUrl);

        try {
            // TMDB API 요청
            Map<String, Object> videosResponse = restTemplate.getForObject(videosUrl, Map.class);
            log.info("API Response for {}: {}", movie.getTitle(), videosResponse);

            if (videosResponse != null && videosResponse.containsKey("results")) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) videosResponse.get("results");

                // YouTube 트레일러 URL 리스트 필터링
                List<String> trailerUrls = results.stream()
                        .filter(video -> "YouTube".equalsIgnoreCase((String) video.get("site")) &&
                                "Trailer".equalsIgnoreCase((String) video.get("type")))
                        .map(video -> "https://www.youtube.com/watch?v=" + video.get("key"))
                        .collect(Collectors.toList());

                if (!trailerUrls.isEmpty()) {
                    // 리스트를 JSON 문자열로 변환
                    String trailersJson = objectMapper.writeValueAsString(trailerUrls);
                    movie.setTrailers(trailersJson);

                    // 영화 엔티티 저장
                    movieRepository.save(movie);
                    log.info("Updated trailers for movie: {} with {} trailer(s)", movie.getTitle(), trailerUrls.size());
                } else {
                    log.warn("No trailers found for movie: {}", movie.getTitle());
                }
            } else {
                log.warn("No results found in API response for movie: {}", movie.getTitle());
            }
        } catch (Exception e) {
            log.error("Error fetching trailers for movie {}: {}", movie.getTitle(), e.getMessage(), e);
        }
    }
}
