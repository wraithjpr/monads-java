package com.thewraithcloud.monads;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Identity class. Wraps a value and is both a functor and a monad.
 *
 * @param <T> is the type to be wrapped
 */
public class Identity<T> {
    // The immutable value wrapped by this Identity.
    private final T value;

    /**
     * Hide default constructor.
     */
    private Identity() {
        this.value = null;
    }

    /**
     * Hide constructor. Use factory functions.
     *
     * @param value is the value to be wrapped
     */
    private Identity(T value) {
        this.value = value;
    }

    /**
     * Factory function. {@code}pure :: a -> Identity a{@code}
     *
     * @param value is the value to be wrapped
     * @return an instance of Identity that wraps value
     */
    public static <V> Identity<V> pure(V value) {
        Objects.requireNonNull(value);

        return new Identity<>(value);
    }

    /**
     * Factory function. {@code}of :: a -> Identity a{@code} Equivalent to and
     * delegates to {@code}pure{@code}
     *
     * @param value is the value to be wrapped
     * @return an instance of Identity that wraps value
     */
    public static <V> Identity<V> of(V value) {
        return Identity.pure(value);
    }

    /**
     * Factory function. {@code}ofProperty :: (() -> a) -> Identity a{@code}
     *
     * @param supplier a getter function where {@code}get :: () -> a{@code}
     * @return an instance of the identity monad containg the value returned by
     *         Supplier::get
     */
    public static <V> Identity<V> ofGetter(Supplier<V> supplier) {
        Objects.requireNonNull(supplier);

        return new Identity<>(supplier.get());
    }

    /**
     * Retrieves the value wrapped by this Identity.
     *
     * @return the wrapped value
     */
    public T get() {
        return value;
    }

    /**
     * The map function of this functor. {@code}map :: (a -> b) -> Identity a ->
     * Identity b{@code}
     *
     * @param fxn the mapper function to apply to the value: {@code}fxn :: a ->
     *            b{@code}
     * @param R   is the type of the returned instance of Identity
     * @return an instance of Identity that wraps the mapped value
     */
    public <R> Identity<R> map(Function<? super T, ? extends R> fxn) {
        Objects.requireNonNull(fxn);

        // final R result = fxn.apply(value);
        // return new Identity<>(result);
        return new Identity<>(fxn.apply(value));
    }

    /**
     * The flatmap function of this functor. {@code}flatmap :: (a -> Identity b) ->
     * Identity a -> Identity b{@code}
     *
     * @param fxn the mapper function to apply to the value: {@code}fxn :: a ->
     *            Identity b{@code}
     * @param R   is the type of the returned instance of Identity
     * @return an instance of Identity that wraps the mapped value
     */
    public <R> Identity<R> flatMap(Function<? super T, Identity<R>> fxn) {
        Objects.requireNonNull(fxn);

        return fxn.apply(value);
    }
}
