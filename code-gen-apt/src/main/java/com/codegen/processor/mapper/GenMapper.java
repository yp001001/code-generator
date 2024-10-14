package com.codegen.processor.mapper;

/**
 * @author: yp
 * @date: 2024/10/14 10:17
 * @description:
 */
public @interface GenMapper {

    String pkgName();

    String sourcePath() default "src/main/java";

    boolean overrideSource() default false;
}
