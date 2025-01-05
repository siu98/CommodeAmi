package com.siuuuuu.commodeami.actor.command.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.siuuuuu.commodeami.actor.command.aggregate.entity.ActorGender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActorDTO {

    @JsonProperty("id")
    private Long actorId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("gender")
    @Enumerated(EnumType.STRING)
    private ActorGender gender;

    @JsonProperty("profile_path")
    private String profileImage;

    @JsonProperty("known_for_department")
    private String knownForDepartment;

    @JsonProperty("original_name")
    private String originalName;
}
