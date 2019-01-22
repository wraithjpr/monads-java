package com.thewraithcloud.monads;

@FunctionalInterface
public interface SupplierThrows<T> {
    T get() throws Throwable;
}
