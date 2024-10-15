package com.codegen.utils.processor.repository;

import java.lang.annotation.*;

/**
 * @author: yp
 * @date: 2024/10/15 16:26
 * @description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenRepository {

    String pkgName();

    String sourcePath() default "src/main/java";

    boolean overrideSource() default false;

}
