package com.codegen.sort;

import java.lang.annotation.*;

/**
 * @author: yp
 * @date: 2024/10/14 15:19
 * @description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Order {

    int sort() default 1;

}
