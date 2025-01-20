//package com.siuuuuu.commodeami.averagescope.command.application.service;
//
//import com.siuuuuu.commodeami.averagescope.command.aggregate.dto.AverageScopeDTO;
//import com.siuuuuu.commodeami.averagescope.command.aggregate.entity.AverageScope;
//import com.siuuuuu.commodeami.averagescope.command.domain.repository.AverageScopeRepository;
//import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
//import com.siuuuuu.commodeami.movie.command.domain.repository.MovieRepository;
//import com.siuuuuu.commodeami.scope.command.aggregate.entity.Scope;
//import com.siuuuuu.commodeami.scope.command.domain.repository.ScopeRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Slf4j
//public class AppAverageScopeServiceImpl implements AppAverageScopeService {
//
//    private final AverageScopeRepository averageScopeRepository;
//    private final MovieRepository movieRepository;
//    private final ScopeRepository scopeRepository;
//
//    @Autowired
//    public AppAverageScopeServiceImpl(AverageScopeRepository averageScopeRepository,
//                                      MovieRepository movieRepository,
//                                      ScopeRepository scopeRepository) {
//        this.averageScopeRepository = averageScopeRepository;
//        this.movieRepository = movieRepository;
//        this.scopeRepository = scopeRepository;
//    }
//
////    @Override
////    @Transactional
////    public AverageScopeDTO saveOrUpdateAverageScope(Long movieId, Double scope, boolean isUpdate) {
////        // 1. 영화 조회
////        Movie movie = movieRepository.findById(movieId)
////                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 영화가 없습니다."));
////
////        // 평균 별점 생성
////        // AverageScope 조회 또는 생성
////        AverageScope averageScope = averageScopeRepository.findByMovie_MovieId(movieId);
////        if (averageScope == null) {
////            averageScope = new AverageScope();
////            averageScope.setMovie(movie);
////            averageScope.setAverageScope(scope);
////            averageScope.setNumberOfPeople(1);
////        } else {
////            // 기존 평균과 새 별점을 이용해 평균 계산
////            int currentCount = averageScope.getNumberOfPeople();
////            double newAverage = ((averageScope.getAverageScope() * currentCount) + scope) / (currentCount + 1);
////
////            averageScope.setAverageScope(newAverage);
////            averageScope.setNumberOfPeople(currentCount + 1);
////        }
////
////        // 4. 저장
////        AverageScope savedScope = averageScopeRepository.save(averageScope);
////
////        // 5. DTO 반환
////        AverageScopeDTO dto = new AverageScopeDTO();
////        dto.setAverageScopeId(savedScope.getAverageScopeId());
////        dto.setAverageScope(savedScope.getAverageScope());
////        dto.setNumberOfPeople(savedScope.getNumberOfPeople());
////        dto.setMovieId(savedScope.getMovie().getMovieId());
////
////        return dto;
////    }
//
//
//    @Override
//    @Transactional
//    public AverageScopeDTO saveOrUpdateAverageScope(Long movieId, Double newScope, Double oldScope, boolean isUpdate) {
//        Movie movie = movieRepository.findById(movieId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 영화가 없습니다."));
//
//        AverageScope averageScope = averageScopeRepository.findByMovie_MovieId(movieId);
//        if (averageScope == null) {
//            // 새로 생성
//            averageScope = new AverageScope();
//            averageScope.setMovie(movie);
//            averageScope.setAverageScope(newScope);
//            averageScope.setNumberOfPeople(1);
//        } else {
//            int currentCount = averageScope.getNumberOfPeople();
//            double newAverage;
//
//            if (isUpdate) {
//                // 기존 별점 수정
//                double currentTotal = averageScope.getAverageScope() * currentCount;
//                newAverage = (currentTotal - oldScope + newScope) / currentCount;
//            } else {
//                // 새로운 별점 추가
//                newAverage = ((averageScope.getAverageScope() * currentCount) + newScope) / (currentCount + 1);
//                averageScope.setNumberOfPeople(currentCount + 1);
//            }
//
//            averageScope.setAverageScope(newAverage);
//        }
//
//        AverageScope savedScope = averageScopeRepository.save(averageScope);
//
//        return new AverageScopeDTO(
//                savedScope.getAverageScopeId(),
//                savedScope.getAverageScope(),
//                savedScope.getNumberOfPeople(),
//                savedScope.getMovie().getMovieId()
//        );
//    }
//}
