package com.siuuuuu.commodeami.scope.command.application.service;

import com.siuuuuu.commodeami.averagescope.command.application.service.AppAverageScopeService;
import com.siuuuuu.commodeami.averagescope.command.domain.repository.AverageScopeRepository;
import com.siuuuuu.commodeami.movie.command.domain.repository.MovieRepository;
import com.siuuuuu.commodeami.review.command.domain.repository.ReviewRepository;
import com.siuuuuu.commodeami.scope.command.aggregate.dto.ScopeDTO;
import com.siuuuuu.commodeami.scope.command.aggregate.entity.Scope;
import com.siuuuuu.commodeami.scope.command.domain.repository.ScopeRepository;
import com.siuuuuu.commodeami.user.command.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppScopeServiceImpl implements AppScopeService {

    private final ScopeRepository scopeRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final AppAverageScopeService appAverageScopeService;

    @Autowired
    public AppScopeServiceImpl(ScopeRepository scopeRepository,
                               UserRepository userRepository,
                               MovieRepository movieRepository,
                               ReviewRepository reviewRepository,
                               AppAverageScopeService appAverageScopeService) {
        this.scopeRepository = scopeRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
        this.appAverageScopeService = appAverageScopeService;
    }

//    @Override
//    public ScopeDTO createOrUpdateScope(Long movieId, Long userId, ScopeDTO scopeDTO) {
//        log.info("Processing userId={}, movieId={}", userId, movieId); // 디버깅 로그
//
//        // 1. 사용자가 있는지 검증
//        if (!userRepository.existsById(userId)) {
//            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: userId=" + userId);
//        }
//
//        // 2. 영화가 있는지 검증
//        if (!movieRepository.existsById(movieId)) {
//            throw new IllegalArgumentException("영화를 찾을 수 없습니다: movieId=" + movieId);
//        }
//
//        // 3. 기존 별점 조회
////        Optional<ScopeDTO> existingScopeOpt = scopeRepository.findByUserIdAndMovieId(userId, movieId);
//        Scope existingScope = scopeRepository.findScopeByUserIdAndMovieId(userId, movieId);
//
//        // 4-1. 기존 별점이 있을 때
//        if (existingScope != null) {
//            // 별점 수정
//            existingScope.setScope(scopeDTO.getScope());
//            existingScope.setCreatedAt(scopeDTO.getCreatedAt());
//            existingScope.setWatchedAt(scopeDTO.getWatchedAt()); // 관람일자 업데이트 (null일 경우 그대로 유지)
//
//            // DTO -> entity 변환
//            Scope updatedScope = new Scope();
//            updatedScope.setScopeId(existingScope.getScopeId());
//            updatedScope.setScope(existingScope.getScope());
//            updatedScope.setCreatedAt(existingScope.getCreatedAt());
//            updatedScope.setWatchedAt(existingScope.getWatchedAt());
//            updatedScope.setUser(userRepository.findById(userId)
//                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")));
//            updatedScope.setMovie(movieRepository.findById(movieId)
//                    .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다.")));
////            updatedScope.setReview(existingScope.getReviewId() != null
////                    ? reviewRepository.findById(existingScope.getReviewId()).orElse(null)
////                    : null);
//
//
//            // 저장
//            scopeRepository.save(updatedScope);
////            return existingScopeDTO;
//            return new ScopeDTO(
//                    updatedScope.getScopeId(),
//                    updatedScope.getScope(),
//                    updatedScope.getCreatedAt(),
//                    updatedScope.getWatchedAt(),
//                    updatedScope.getUser().getUserId(),
//                    updatedScope.getMovie().getMovieId()
////                    updatedScope.getReview() != null ? updatedScope.getReview().getReviewId() : null
//            );
//        } else {
//            // 4-2. 기존 별점이 없을 때
//            Scope newScope = new Scope();
//            newScope.setScope(scopeDTO.getScope());
//            newScope.setCreatedAt(scopeDTO.getCreatedAt());
//            newScope.setUser(userRepository.findById(userId)
//                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")));
//            newScope.setMovie(movieRepository.findById(movieId)
//                    .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다.")));
////            newScope.setReview(scopeDTO.getReviewId() != null
////                    ? reviewRepository.findById(scopeDTO.getReviewId()).orElse(null)
////                    : null);
//
//            // 저장
//            Scope savedScope = scopeRepository.save(newScope);
//
////            updateAverageScope(movieId, savedScope.getScope(), existingScope != null);
//
//            // entity -> DTO 변환
//            return new ScopeDTO(
//                    savedScope.getScopeId(),
//                    savedScope.getScope(),
//                    savedScope.getCreatedAt(),
//                    savedScope.getWatchedAt(), // 관람일자 반환
//                    savedScope.getUser().getUserId(),
//                    savedScope.getMovie().getMovieId()
////                    savedScope.getReview() != null ? savedScope.getReview().getReviewId() : null
////                    savedScope.getReview() != null ? savedScope.getReview().getReviewId() : null,
//
//
//            );
//        }
//    }

    @Override
    public ScopeDTO createOrUpdateScope(Long movieId, Long userId, ScopeDTO scopeDTO) {
        log.info("Processing userId={}, movieId={}", userId, movieId); // 디버깅 로그

        // 1. 사용자가 있는지 검증
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: userId=" + userId);
        }

        // 2. 영화가 있는지 검증
        if (!movieRepository.existsById(movieId)) {
            throw new IllegalArgumentException("영화를 찾을 수 없습니다: movieId=" + movieId);
        }

        // 3. 기존 별점 조회
        Scope existingScope = scopeRepository.findScopeByUserIdAndMovieId(userId, movieId);

        // 변수 선언
        boolean isUpdate = (existingScope != null);
        Double oldScope = isUpdate ? existingScope.getScope() : null;

        // 4-1. 기존 별점이 있을 때
        if (isUpdate) {
            existingScope.setScope(scopeDTO.getScope());
            existingScope.setCreatedAt(scopeDTO.getCreatedAt());
            existingScope.setWatchedAt(scopeDTO.getWatchedAt()); // 관람일자 업데이트 (null일 경우 그대로 유지)

            // 저장
            scopeRepository.save(existingScope);

            // 평균 별점 업데이트
            appAverageScopeService.saveOrUpdateAverageScope(movieId, scopeDTO.getScope(), oldScope, true);

            return new ScopeDTO(
                    existingScope.getScopeId(),
                    existingScope.getScope(),
                    existingScope.getCreatedAt(),
                    existingScope.getWatchedAt(),
                    existingScope.getUser().getUserId(),
                    existingScope.getMovie().getMovieId()
            );
        } else {
            // 4-2. 기존 별점이 없을 때
            Scope newScope = new Scope();
            newScope.setScope(scopeDTO.getScope());
            newScope.setCreatedAt(scopeDTO.getCreatedAt());
            newScope.setUser(userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")));
            newScope.setMovie(movieRepository.findById(movieId)
                    .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다.")));

            // 저장
            Scope savedScope = scopeRepository.save(newScope);

            // 평균 별점 업데이트
            appAverageScopeService.saveOrUpdateAverageScope(movieId, scopeDTO.getScope(), null, false);

            return new ScopeDTO(
                    savedScope.getScopeId(),
                    savedScope.getScope(),
                    savedScope.getCreatedAt(),
                    savedScope.getWatchedAt(),
                    savedScope.getUser().getUserId(),
                    savedScope.getMovie().getMovieId()
            );
        }
    }


    @Override
    public ScopeDTO deleteScope(Long scopeId) {
        // 1. Scope 존재 여부 확인
        Scope scope = scopeRepository.findById(scopeId)
                .orElseThrow(() -> new IllegalArgumentException("별점을 찾을 수 없습니다: scopeId=" + scopeId));

        // 2. Scope 삭제
        scopeRepository.deleteById(scopeId);

        // 3. 삭제된 Scope를 DTO로 반환
        return new ScopeDTO(
                scope.getScopeId(),
                scope.getScope(),
                scope.getCreatedAt(),
                scope.getWatchedAt(),
                scope.getUser().getUserId(),
                scope.getMovie().getMovieId()
//                scope.getReview() != null ? scope.getReview().getReviewId() : null // 리뷰 ID 포함
        );
    }

    @Override
    public ScopeDTO createOrUpdateWatchedAt(Long scopeId, ScopeDTO scopeDTO) {
        // 1. 별점 존재 여부 확인
        Scope scope = scopeRepository.findById(scopeId)
                .orElseThrow(() -> new IllegalArgumentException("별점을 찾을 수 없습니다: scopeId=" + scopeId));

        // 2. 관람일자 추가 또는 수정
        scope.setWatchedAt(scopeDTO.getWatchedAt());
        Scope updatedScope = scopeRepository.save(scope);

        // 3. DTO 반환
        return new ScopeDTO(
                updatedScope.getScopeId(),
                updatedScope.getScope(),
                updatedScope.getCreatedAt(),
                updatedScope.getWatchedAt(),
                scope.getUser().getUserId(),
                scope.getMovie().getMovieId()
//                scope.getReview() != null ? scope.getReview().getReviewId() : null // 리뷰 ID 포함
        );
    }

    @Override
    public ScopeDTO deleteWatchedAt(Long scopeId) {
        // 1. 별점 존재여부 확인
        Scope scope = scopeRepository.findById(scopeId)
                .orElseThrow(() -> new IllegalArgumentException("별점을 찾을 수 없습니다: scopeId=" + scopeId));

        // 2. 관람일자 존재 여부 확인
        if (scope.getWatchedAt() == null) {
            throw new IllegalArgumentException("관람일자가 설정되어 있지 않습니다: scopeId=" + scopeId);
        }

        // 3. 관람일자 삭제
        scope.setWatchedAt(null);
        Scope updatedScope = scopeRepository.save(scope);

        // 4. DTO 반환
        return new ScopeDTO(
                updatedScope.getScopeId(),
                updatedScope.getScope(),
                updatedScope.getCreatedAt(),
                updatedScope.getWatchedAt(),
                scope.getUser().getUserId(),
                scope.getMovie().getMovieId()
//                scope.getReview() != null ? scope.getReview().getReviewId() : null // 리뷰 ID 포함
        );
    }
}
