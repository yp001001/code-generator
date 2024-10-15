package com.codegen.utils.processor.service;

import java.lang.annotation.*;

/**
 * @author: yp
 * @date: 2024/10/14 14:46
 * @description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenService {

    String pkgName();

    String sourcePath() default "src/main/java";

    boolean overrideSource() default false;
}
