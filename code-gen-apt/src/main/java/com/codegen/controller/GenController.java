package com.codegen.controller;

import java.lang.annotation.*;

/**
 * @author: yp
 * @date: 2024/10/12 15:24
 * @description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenController {

    String pkgName();

    String sourcePath() default "src/main/java";

    boolean overrideSource() default false;
}
