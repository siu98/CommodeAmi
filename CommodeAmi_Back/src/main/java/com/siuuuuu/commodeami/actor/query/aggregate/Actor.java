package com.siuuuuu.commodeami.actor.query.aggregate;

import com.siuuuuu.commodeami.actor.command.aggregate.entity.ActorGender;
import lombok.Data;

@Data
public class Actor {

    Long actorId;
    String name;
    ActorGender gender;
    String profileImage;
    String knownForDepartment;
    String originalName;
}
