package com.jpa.support;

import org.springframework.data.repository.CrudRepository;

/**
 * @author: yp
 * @date: 2024/10/15 10:21
 * @description:
 */
public abstract class EntityOperations {

    public static <T, ID> EntityUpdater<T, ID> doUpdate(CrudRepository<T, ID> repository) {
        return new EntityUpdater<>(repository);
    }

    public static <T, ID> EntityCreator<T, ID> doCreate(CrudRepository<T, ID> repository) {
        return new EntityCreator(repository);
    }

}
