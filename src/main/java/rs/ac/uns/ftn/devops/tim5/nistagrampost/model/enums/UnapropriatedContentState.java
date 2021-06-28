package rs.ac.uns.ftn.devops.tim5.nistagrampost.model.enums;

import java.util.stream.Stream;

public enum UnapropriatedContentState {

    REQUESTED(1), CONFIRMED(2), REJECTED(3);

    private int value;

    private UnapropriatedContentState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UnapropriatedContentState of(int value) {
        return Stream.of(UnapropriatedContentState.values())
                .filter(p -> p.getValue() == value)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

