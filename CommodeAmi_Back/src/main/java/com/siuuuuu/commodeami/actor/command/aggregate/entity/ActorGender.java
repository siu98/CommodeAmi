package com.siuuuuu.commodeami.actor.command.aggregate.entity;

public enum ActorGender {
    NOT_SPECIFIED(0, "Not Specified"),
    FEMALE(1, "Female"),
    MALE(2, "Male"),
    NON_BINARY(3, "Non-binary");
    private final int code;
    private final String description;

    ActorGender(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ActorGender fromCode(int code) {
        for (ActorGender gender : ActorGender.values()) {
            if (gender.getCode() == code) {
                return gender;
            }
        }
        return NOT_SPECIFIED; // 기본값 반환
    }
}
