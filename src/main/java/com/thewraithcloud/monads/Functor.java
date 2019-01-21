package com.thewraithcloud.monads;

import java.util.function.Function;

interface Functor<T, F extends Functor<?, ?>> {
    // map :: (a -> b) -> Functor a -> Functor b
    <R> F map(Function<T, R> f);
}
