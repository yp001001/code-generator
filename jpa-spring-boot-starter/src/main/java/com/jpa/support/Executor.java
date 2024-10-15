package com.jpa.support;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author: yp
 * @date: 2024/10/15 10:28
 * @description:
 */
public interface Executor<T> {

    Optional<T> execute();

    Executor<T> successHook(Consumer<T> consumer);

    Executor<T> errorHook(Consumer<? super Throwable> consumer);

}
