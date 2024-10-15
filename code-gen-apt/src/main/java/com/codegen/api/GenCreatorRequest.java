package com.codegen.api;

import java.lang.annotation.*;

/**
 * @author: yp
 * @date: 2024/10/15 9:42
 * @description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenCreatorRequest {

    String pkgName();

    String sourcePath() default "src/main/java";

    boolean overrideSource() default false;
}