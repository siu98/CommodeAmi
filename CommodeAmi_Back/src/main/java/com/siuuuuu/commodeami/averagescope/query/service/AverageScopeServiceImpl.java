package com.siuuuuu.commodeami.averagescope.query.service;

import com.siuuuuu.commodeami.averagescope.query.aggregate.AverageScope;
import com.siuuuuu.commodeami.averagescope.query.aggregate.AverageScopeDTO;
import com.siuuuuu.commodeami.averagescope.query.repository.AverageScopeMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AverageScopeServiceImpl implements AverageScopeService {

    private final AverageScopeMapper averageScopeMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public AverageScopeServiceImpl(AverageScopeMapper averageScopeMapper,
                                   ModelMapper modelMapper) {
        this.averageScopeMapper = averageScopeMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<AverageScopeDTO> getAllAverageScopes() {
        List<AverageScope> averageScopes = averageScopeMapper.selectAllAverageScopes();
        List<AverageScopeDTO> averageScopeDTOS =
                averageScopes.stream().map(averageScope -> modelMapper.map(averageScope, AverageScopeDTO.class)).collect(Collectors.toList());
        return averageScopeDTOS;
    }

    @Override
    public AverageScopeDTO getAverageScopeByMovieId(Long movieId) {
        AverageScope averageScope = averageScopeMapper.selectAverageScopeByMovieId(movieId);

        if (averageScope == null) {
            log.info("해당 영화에 대한 평가 데이터가 없습니다: movieId=" + movieId);
// 기본값을 반환
            AverageScopeDTO defaultScope = new AverageScopeDTO();
            defaultScope.setAverageScope(0.0); // 기본 평균 별점
            defaultScope.setNumberOfPeople(0); // 평가 인원 수
            defaultScope.setMovieId(movieId); // 요청된 영화 ID 반환
            return defaultScope;
        }

        return modelMapper.map(averageScope, AverageScopeDTO.class);
    }
}
