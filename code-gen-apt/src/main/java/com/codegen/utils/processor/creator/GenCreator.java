package com.codegen.utils.processor.creator;

import java.lang.annotation.*;

/**
 * @author: yp
 * @date: 2024/10/15 9:36
 * @description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenCreator {

    String pkgName();

    String sourcePath() default "src/main/java";

    boolean overrideSource() default false;
}
