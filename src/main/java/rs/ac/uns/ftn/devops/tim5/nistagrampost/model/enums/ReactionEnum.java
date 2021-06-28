package rs.ac.uns.ftn.devops.tim5.nistagrampost.model.enums;

import rs.ac.uns.ftn.devops.tim5.nistagrampost.model.Reaction;

import java.util.stream.Stream;

public enum  ReactionEnum {
    LIKE(1), DISLIKE(2);

    private int value;

    private ReactionEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ReactionEnum of(int value) {
        return Stream.of(ReactionEnum.values())
                .filter(p -> p.getValue() == value)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}