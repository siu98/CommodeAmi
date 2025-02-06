package com.siuuuuu.commodeami.actor.command.application.service;

import com.siuuuuu.commodeami.actor.command.aggregate.dto.ActorDTO;
import com.siuuuuu.commodeami.actor.command.aggregate.entity.Actor;
import com.siuuuuu.commodeami.actor.command.domain.repository.ActorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppActorServiceImpl implements AppActorService {

    private final ActorRepository actorRepository;

    public AppActorServiceImpl(ActorRepository actorRepository) {
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
                    newActor.setGender(actorDTO.getGender());
                    newActor.setOriginalName(actorDTO.getOriginalName());
                    newActor.setKnownForDepartment(actorDTO.getKnownForDepartment());
                    Actor savedActor = actorRepository.save(newActor);
                    log.info("Created new actor: {}", savedActor);
                    return savedActor;
                });
    }

}
