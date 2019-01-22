package com.thewraithcloud.monads;

import java.util.Objects;

@FunctionalInterface
public interface FunctionThrows<T, R> {

    R apply(T t) throws Throwable;

    default <V> FunctionThrows<V, R> compose(FunctionThrows<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> FunctionThrows<T, V> andThen(FunctionThrows<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

}
