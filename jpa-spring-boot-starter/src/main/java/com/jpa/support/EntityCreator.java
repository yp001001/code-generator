package com.jpa.support;

import com.codegen.common.validator.CreateGroup;
import com.google.common.base.Preconditions;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author: yp
 * @date: 2024/10/15 10:53
 * @description:
 */
@Slf4j
public class EntityCreator<T, ID> extends BaseEntityOperation implements Create<T>, UpdateHandler<T>, Executor<T> {

    private final CrudRepository<T, ID> repository;
    private T t;
    private Consumer<T> successHook = t -> log.info("save success");
    private Consumer<? super Throwable> errorHook = e -> e.printStackTrace();

    public EntityCreator(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public Executor<T> errorHook(Consumer<? super Throwable> consumer) {
        this.errorHook = consumer;
        return this;
    }

    @Override
    public UpdateHandler<T> create(Supplier<T> supplier) {
        this.t = supplier.get();
        return this;
    }

    @Override
    public Executor<T> update(Consumer<T> consumer) {
        Preconditions.checkArgument(Objects.nonNull(t), "entity must supply");
        consumer.accept(this.t);
        return this;
    }

    @Override
    public Optional<T> execute() {
        doValidate(this.t, CreateGroup.class);
        T save = Try.of(() -> repository.save(t))
                .onSuccess(successHook)
                .onFailure(errorHook).getOrNull();
        return Optional.ofNullable(save);
    }

    @Override
    public Executor<T> successHook(Consumer<T> consumer) {
        this.successHook = consumer;
        return this;
    }

}
