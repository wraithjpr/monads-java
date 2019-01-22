package com.thewraithcloud.monads;

import java.util.Objects;

@FunctionalInterface
public interface ConsumerThrows<T> {

    void accept(T t) throws Throwable;

    default ConsumerThrows<T> andThen(ConsumerThrows<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }
}
