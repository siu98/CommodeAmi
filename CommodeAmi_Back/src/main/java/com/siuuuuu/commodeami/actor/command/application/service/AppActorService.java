package com.siuuuuu.commodeami.actor.command.application.service;

import com.siuuuuu.commodeami.actor.command.aggregate.dto.ActorDTO;
import com.siuuuuu.commodeami.actor.command.aggregate.entity.Actor;

public interface AppActorService {
    Actor saveOrUpdateActor(ActorDTO actorDTO);
}
