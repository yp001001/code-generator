package com.codegen.query;

import java.lang.annotation.*;

/**
 * @Author: Gim
 * @Date: 2019-10-08 17:34
 * @Description:
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryItem {
  String desc() default "";
}
