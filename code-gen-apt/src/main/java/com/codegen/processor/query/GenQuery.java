package com.codegen.processor.query;

import java.lang.annotation.*;

/**
 * @Author: Gim
 * @Date: 2019-10-08 17:13
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenQuery {

  String pkgName();

  String sourcePath() default "src/main/java";

  boolean overrideSource() default false;
}
