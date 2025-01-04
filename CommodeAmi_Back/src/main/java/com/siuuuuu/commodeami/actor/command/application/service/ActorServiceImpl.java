package com.siuuuuu.commodeami.actor.command.application.service;

import com.siuuuuu.commodeami.actor.command.aggregate.dto.ActorDTO;
import com.siuuuuu.commodeami.actor.command.aggregate.entity.Actor;
import com.siuuuuu.commodeami.actor.command.aggregate.entity.ActorGender;
import com.siuuuuu.commodeami.actor.command.domain.repository.ActorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;

    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
//    @Transactional
    public Actor saveOrUpdateActor(ActorDTO actorDTO) {
        return actorRepository.findByIdWithLock(actorDTO.getActorId())
                .map(existingActor -> {
                    log.info("Actor with actorId: {} found in database. Updating existing actor.", actorDTO.getActorId());
                    existingActor.setName(actorDTO.getName());
                    existingActor.setProfileImage(actorDTO.getProfileImage());
                    existingActor.setGender(actorDTO.getGender());
                    existingActor.setOriginalName(actorDTO.getOriginalName());
                    existingActor.setKnownForDepartment(actorDTO.getKnownForDepartment());
                    return actorRepository.save(existingActor);
                })
                .orElseGet(() -> {
                    log.info("Actor with actorId: {} not found. Creating a new actor.", actorDTO.getActorId());
                    Actor newActor = new Actor();
                    newActor.setActorId(actorDTO.getActorId());
                    newActor.setName(actorDTO.getName());
                    newActor.setProfileImage(actorDTO.getProfileImage());
                    newActor.setGender(actorDTO.getGender()); // 직접 설정
                    newActor.setOriginalName(actorDTO.getOriginalName());
                    newActor.setKnownForDepartment(actorDTO.getKnownForDepartment());
                    // version은 자동으로 관리되지만 필요시 명시적 초기화
//                    if (newActor.getVersion() == null) {
//                        newActor.setVersion(0);
//                    }
//                    return actorRepository.save(newActor);
                    Actor savedActor = actorRepository.save(newActor);
                    log.info("Created new actor: {}", savedActor);
                    return savedActor;
                });
    }

}
