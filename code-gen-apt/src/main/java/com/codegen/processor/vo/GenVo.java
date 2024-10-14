package com.codegen.processor.vo;

import java.lang.annotation.*;

/**
 * @author: yp
 * @date: 2024/10/12 15:35
 * @description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenVo {

    String pkgName();

    String sourcePath() default "src/main/java";

    boolean overrideSource() default false;

    boolean jpa() default true;
}
