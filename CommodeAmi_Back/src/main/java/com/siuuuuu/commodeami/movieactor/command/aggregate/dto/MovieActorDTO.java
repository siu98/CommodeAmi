package com.siuuuuu.commodeami.movieactor.command.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.siuuuuu.commodeami.actor.command.aggregate.entity.Actor;
import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieActorDTO {

//    @JsonProperty("movie_actor_id")
    private Long movieActorId;

    @JsonProperty("character")
    private String role;

    @JsonProperty("order")
    private Integer castingOrder;

    private Actor actor;
    private Movie movie;
}
