package io.github.ramboxeu.techworks.common.util.function;

@FunctionalInterface
public interface TriConsumer<T, U, W> {
    void accept(T t, U u, W w);
}
