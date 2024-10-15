package com.codegen.utils.processor.update;

import java.lang.annotation.*;

/**
 * @author: yp
 * @date: 2024/10/15 13:35
 * @description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenUpdate {

    String pkgName();

    String sourcePath() default "src/main/java";

    boolean overrideSource() default false;

}
