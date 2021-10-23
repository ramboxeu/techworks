package io.github.ramboxeu.techworks.common.util;

public enum RedstoneMode {
    IGNORE(powered -> true),
    HIGH(powered -> powered),
    LOW(powered -> !powered);

    private final WorkPredicate predicate;

    RedstoneMode(WorkPredicate predicate) {
        this.predicate = predicate;
    }

    public boolean canWork(boolean powered) {
        return predicate.canWork(powered);
    }

    public RedstoneMode next() {
        switch (this) {
            case IGNORE:
                return HIGH;
            case HIGH:
                return LOW;
            case LOW:
                return IGNORE;
        }

        throw new AssertionError();
    }

    public RedstoneMode previous() {
        switch (this) {
            case IGNORE:
                return LOW;
            case HIGH:
                return IGNORE;
            case LOW:
                return HIGH;
        }

        throw new AssertionError();
    }

    @FunctionalInterface
    private interface WorkPredicate {
        boolean canWork(boolean powered);
    }
}
