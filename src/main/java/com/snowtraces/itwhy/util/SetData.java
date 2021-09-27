package com.snowtraces.itwhy.util;

import java.lang.annotation.*;

/**
 * @author snow
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SetData {
    /**
     * 来源字段
     *
     * @return
     */
    String value() default "";
}