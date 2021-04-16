package io.github.ramboxeu.techworks.common.util;

public class Pair<T, U> {
    private final T left;
    private final U right;

    public Pair(T left, U right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public U getRight() {
        return right;
    }

    public static <T, U> Pair<T, U> of(T left, U right) {
        return new Pair<>(left, right);
    }
}
