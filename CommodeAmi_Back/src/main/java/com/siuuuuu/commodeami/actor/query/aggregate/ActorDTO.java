package com.siuuuuu.commodeami.actor.query.aggregate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.siuuuuu.commodeami.actor.command.aggregate.entity.ActorGender;
import lombok.Data;

@Data
public class ActorDTO {

    @JsonProperty("actor_id")
    Long actorId;

    @JsonProperty("name")
    String name;

    @JsonProperty("gender")
    ActorGender gender;

    @JsonProperty("profile_image")
    String profileImage;

    @JsonProperty("known_for_department")
    String knownForDepartment;

    @JsonProperty("original_name")
    String originalName;
}
