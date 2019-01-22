package com.thewraithcloud.monads;

import java.util.Objects;
// import java.util.function.Function;
// import java.util.function.Supplier;

/**
 * Result class. Wraps a value and is both a functor and a monad.
 *
 * @param <T> is the type to be wrapped
 */
public abstract class Result<T> {

    /**
     * Default constructor.
     */
    protected Result() {
    }

    /**
     * Factory function. {@code ofSuccessful :: a -> Success a}
     *
     * @param value is the value to be wrapped
     * @return an instance of Result that wraps value
     */
    public static <V> Result<V> ofSuccessful(V value) {
        Objects.requireNonNull(value);

        return Success.of(value);
    }

    /**
     * Factory function. {@code ofFailed :: e -> Failure e}
     *
     * @param value is the value to be wrapped
     * @return an instance of Result that wraps value
     */
    public static <V> Result<V> ofFailed(Throwable e) {
        Objects.requireNonNull(e);

        return Failure.of(e);
    }

    /**
     * Factory function. {@code ofProperty :: (() -> a) -> Success a | Failure}
     *
     * @param supplier a getter function where {@code get :: () -> a}
     * @return an instance of the identity monad containg the value returned by
     *         Supplier::get
     */
    public static <V> Result<V> ofFailable(SupplierThrows<V> supplier) {
        Objects.requireNonNull(supplier);

        try {
            return Result.ofSuccessful(supplier.get());
        } catch (Throwable e) {
            return Result.ofFailed(e);
        }
    }

    /**
     * Retrieves the value wrapped by this Result.
     *
     * @return the wrapped value
     * @throws Throwable
     */
    public abstract T get() throws Throwable;

    /**
     * The map function of this functor. {@code map :: (a -> b) -> Result a ->
     * Result b}
     *
     * @param fxn the mapper function to apply to the value: {@code}fxn :: a ->
     *            b{@code}
     * @param R   is the type of the returned instance of Result
     * @return an instance of Result that wraps the mapped value
     */
    public abstract <R> Result<R> map(FunctionThrows<? super T, ? extends R> fxn);

    /**
     * The flatmap function of this functor. {@code flatmap :: (a -> Result b) ->
     * Result a -> Result b}
     *
     * @param fxn the mapper function to apply to the value: {@code fxn :: a ->
     *            Result b}
     * @param R   is the type of the returned instance of Result
     * @return an instance of Result that wraps the mapped value
     */
    public abstract <R> Result<R> flatMap(FunctionThrows<? super T, Result<R>> fxn);
}

class Success<T> extends Result<T> {
    // The immutable value wrapped by this Result.
    private final T value;

    /**
     * Hide default constructor.
     */
    private Success() {
        this.value = null;
    }

    /**
     * Hide constructor. Use factory functions.
     *
     * @param value is the value to be wrapped
     */
    private Success(T value) {
        this.value = value;
    }

    /**
     * Factory function. {@code of :: a -> Result a} Equivalent to and delegates to
     *
     * @param value is the value to be wrapped
     * @return an instance of Result that wraps value
     */
    public static <V> Result<V> of(V value) {
        Objects.requireNonNull(value);

        return new Success<>(value);
    }

    /**
     * Retrieves the value wrapped by this Result.
     *
     * @return the wrapped value
     */
    @Override
    public T get() throws Throwable {
        return value;
    }

    /**
     * The map function of this functor. {@code map :: (a -> b) -> Result a ->
     * Result b}
     *
     * @param fxn the mapper function to apply to the value: {@code fxn :: a ->
     *            b}
     * @param R   is the type of the returned instance of Result
     * @return an instance of Result that wraps the mapped value
     */
    @Override
    public <R> Result<R> map(FunctionThrows<? super T, ? extends R> fxn) {
        Objects.requireNonNull(fxn);

        try {
            return new Success<>(fxn.apply(value));
        } catch (Throwable e) {
            return Result.ofFailed(e);
        }
    }

    /**
     * The flatmap function of this functor. {@code flatmap :: (a -> Result b) ->
     * Result a -> Result b}
     *
     * @param fxn the mapper function to apply to the value: {@code fxn :: a ->
     *            Result b}
     * @param R   is the type of the returned instance of Result
     * @return an instance of Result that wraps the mapped value
     */
    @Override
    public <R> Result<R> flatMap(FunctionThrows<? super T, Result<R>> fxn) {
        Objects.requireNonNull(fxn);

        try {
            return fxn.apply(value);
        } catch (Throwable e) {
            return Result.ofFailed(e);
        }
    }
}

class Failure<T> extends Result<T> {
    // The immutable Throwable wrapped by this Result.
    private final Throwable e;

    /**
     * Hide default constructor.
     */
    private Failure() {
        this.e = null;
    }

    /**
     * Hide constructor. Use factory functions.
     *
     * @param e is the Throwable to be wrapped
     */
    private Failure(Throwable e) {
        this.e = e;
    }

    /**
     * Factory function. {@code of :: a -> Result a} Equivalent to and delegates to
     *
     * @param e is the Throwable to be wrapped
     * @return an instance of Result that wraps e
     */
    public static <V> Result<V> of(Throwable e) {
        Objects.requireNonNull(e);

        return new Failure<>(e);
    }

    @Override
    public T get() throws Throwable {
        throw e;
    }

    @Override
    public <R> Result<R> map(FunctionThrows<? super T, ? extends R> fxn) {
        Objects.requireNonNull(fxn);

        return Result.ofFailed(e);
    }

    @Override
    public <R> Result<R> flatMap(FunctionThrows<? super T, Result<R>> fxn) {
        Objects.requireNonNull(fxn);

        return Result.ofFailed(e);
    }
}
