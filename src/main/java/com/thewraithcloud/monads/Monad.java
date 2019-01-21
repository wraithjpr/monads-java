package com.thewraithcloud.monads;

import java.util.function.Function;

interface Monad<T, M extends Monad<?, ?>> extends Functor<T, M> {
    // flatmap :: (a -> Monad b) -> Monad a -> Monad b
    M flatMap(Function<T, M> f);
}
