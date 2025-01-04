package com.siuuuuu.commodeami.actor.command.domain.repository;


import com.siuuuuu.commodeami.actor.command.aggregate.entity.Actor;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
//    Optional<Actor> findByName(String name);
@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Actor a WHERE a.name = :name")
    Optional<Actor> findByName(@Param("name") String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Actor a WHERE a.actorId = :actorId")
    Optional<Actor> findByIdWithLock(@Param("actorId") Long actorId);
}
