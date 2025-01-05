package com.siuuuuu.commodeami.movieactor.command.aggregate.entity;

import com.siuuuuu.commodeami.actor.command.aggregate.entity.Actor;
import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "TBL_MOVIE_ACTOR", uniqueConstraints = @UniqueConstraint(columnNames = {"movie_id", "actor_id"}))
public class MovieActor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="movie_actor_id")
    private Long movieActorId;

    @Column(name="role")
    private String role;

    @Column(name="casting_order")
    private Integer castingOrder;

    @ManyToOne
    @JoinColumn(name="actor_id")
    private Actor actor;

    @ManyToOne
    @JoinColumn(name="movie_id")
    private Movie movie;
}
