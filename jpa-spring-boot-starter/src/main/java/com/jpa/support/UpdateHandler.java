package com.jpa.support;

import java.util.function.Consumer;

/**
 * @author: yp
 * @date: 2024/10/15 10:27
 * @description:
 */
public interface UpdateHandler<T>{

    Executor<T> update(Consumer<T> consumer);

}
