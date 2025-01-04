package com.siuuuuu.commodeami.actor.command.aggregate.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ActorGenderConverter implements AttributeConverter<ActorGender, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ActorGender gender) {
        return gender != null ? gender.getCode() : ActorGender.NOT_SPECIFIED.getCode();
    }

    @Override
    public ActorGender convertToEntityAttribute(Integer code) {
        return ActorGender.fromCode(code);
    }
}
