package com.siuuuuu.commodeami.actor.command.aggregate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="TBL_ACTOR")
public class Actor {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="actor_id")
    private Long actorId;

    @Column(name="name")
    private String name;

    @Column(name="gender")
    @Convert(converter = ActorGenderConverter.class)
    private ActorGender gender;

    @Column(name="profile_image")
    private String profileImage;

    @Column(name="known_for_department")
    private String knownForDepartment;

    @Column(name="original_name")
    private String originalName;

//    @Version
//    private Integer version;
}
