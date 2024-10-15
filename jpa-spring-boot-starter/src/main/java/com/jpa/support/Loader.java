package com.jpa.support;

import java.util.function.Supplier;

/**
 * @author: yp
 * @date: 2024/10/15 10:27
 * @description:
 */
public interface Loader<T, ID>{

    UpdateHandler<T> loadById(ID id);

    UpdateHandler<T> load(Supplier<T> t);

}
