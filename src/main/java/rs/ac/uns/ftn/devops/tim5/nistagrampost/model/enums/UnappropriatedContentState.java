package rs.ac.uns.ftn.devops.tim5.nistagrampost.model.enums;

import java.util.stream.Stream;

public enum UnappropriatedContentState {

    REQUESTED(1), CONFIRMED(2), REJECTED(3);

    private int value;

    UnappropriatedContentState(int value) {
        this.value = value;
    }

    public static UnappropriatedContentState of(int value) {
        return Stream.of(UnappropriatedContentState.values())
                .filter(p -> p.getValue() == value)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int getValue() {
        return value;
    }
}

