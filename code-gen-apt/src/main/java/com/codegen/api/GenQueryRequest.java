package com.codegen.api;

import java.lang.annotation.*;

/**
 * @author gim
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenQueryRequest {

  String pkgName();

  String sourcePath() default "src/main/java";

  boolean overrideSource() default false;
}
