package com.codegen.utils.processor.mapper;

import java.lang.annotation.*;

/**
 * @author: yp
 * @date: 2024/10/14 10:17
 * @description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenMapper {

    String pkgName();

    String sourcePath() default "src/main/java";

    boolean overrideSource() default false;
}
