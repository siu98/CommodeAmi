package com.siuuuuu.commodeami.averagescope.command.application.service;

import com.siuuuuu.commodeami.averagescope.command.aggregate.dto.AverageScopeDTO;
import com.siuuuuu.commodeami.averagescope.command.aggregate.entity.AverageScope;
import com.siuuuuu.commodeami.averagescope.command.domain.repository.AverageScopeRepository;
import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import com.siuuuuu.commodeami.movie.command.domain.repository.MovieRepository;
import com.siuuuuu.commodeami.scope.command.aggregate.entity.Scope;
import com.siuuuuu.commodeami.scope.command.domain.repository.ScopeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AppAverageScopeServiceImpl implements AppAverageScopeService {

    private final AverageScopeRepository averageScopeRepository;
    private final MovieRepository movieRepository;
    private final ScopeRepository scopeRepository;

    @Autowired
    public AppAverageScopeServiceImpl(AverageScopeRepository averageScopeRepository,
                                      MovieRepository movieRepository,
                                      ScopeRepository scopeRepository) {
        this.averageScopeRepository = averageScopeRepository;
        this.movieRepository = movieRepository;
        this.scopeRepository = scopeRepository;
    }


    @Override
    @Transactional
    public AverageScopeDTO saveOrUpdateAverageScope(Long movieId, Double newScope, Double oldScope, boolean isUpdate) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 영화가 없습니다."));

        AverageScope averageScope = averageScopeRepository.findByMovie_MovieId(movieId);
        if (averageScope == null) {
            // 새로 생성
            averageScope = new AverageScope();
            averageScope.setMovie(movie);

            averageScope.setAverageScope(newScope);
            averageScope.setNumberOfPeople(1);
        } else {
            int currentCount = averageScope.getNumberOfPeople();
            double newAverage;

            if (isUpdate) {
                // 기존 별점 수정
                double currentTotal = averageScope.getAverageScope() * currentCount;
                newAverage = (currentTotal - oldScope + newScope) / currentCount;
            } else {
                // 새로운 별점 추가
                newAverage = ((averageScope.getAverageScope() * currentCount) + newScope) / (currentCount + 1);
                averageScope.setNumberOfPeople(currentCount + 1);
            }

            averageScope.setAverageScope(newAverage);

            // 소수점 둘째 자리에서 반올림하고 첫째 자리까지 표현
//            double roundedAverage = Math.round(newAverage * 10.0) / 10.0;
//            averageScope.setAverageScope(roundedAverage);
        }

        AverageScope savedScope = averageScopeRepository.save(averageScope);

        return new AverageScopeDTO(
                savedScope.getAverageScopeId(),
                savedScope.getAverageScope(),
                savedScope.getNumberOfPeople(),
                savedScope.getMovie().getMovieId()
        );
    }
}
