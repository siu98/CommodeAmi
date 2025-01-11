package com.siuuuuu.commodeami.actor.query.aggregate;

import com.siuuuuu.commodeami.actor.command.aggregate.entity.ActorGender;
import lombok.Data;

@Data
public class MovieActor {

    Long actorId;
    String name;
    String gender;
    String profileImage;
    String knownForDepartment;
    String originalName;
    String role;
    Integer castingOrder;
//    Long movieId;

}
