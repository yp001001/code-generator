package com.jpa.support;

import java.util.function.Supplier;

/**
 * @author: yp
 * @date: 2024/10/15 10:53
 * @description:
 */
public interface Create<T> {

    UpdateHandler<T> create(Supplier<T> supplier);

}
