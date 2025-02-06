package com.siuuuuu.commodeami.scope.command.aggregate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScopeDTO {

    private Long scopeId;
    private Double scope;
    private LocalDateTime createdAt;
    private Date watchedAt;
    private Long movieId;
    private Long userId;

}
