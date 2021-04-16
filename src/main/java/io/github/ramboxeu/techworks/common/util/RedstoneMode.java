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

    @FunctionalInterface
    private interface WorkPredicate {
        boolean canWork(boolean powered);
    }
}
