package com.siuuuuu.commodeami.review.command.aggregate.entity;

import com.siuuuuu.commodeami.movie.command.aggregate.entity.Movie;
import com.siuuuuu.commodeami.scope.command.aggregate.entity.Scope;
import com.siuuuuu.commodeami.user.command.aggregate.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="TBL_REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="review_id")
    private Long reviewId;

    // 생성일
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    // 리뷰
    @Column(name="review")
    private String review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="scope_id")
    private Scope scope;
}
