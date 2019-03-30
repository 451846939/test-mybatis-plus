package com.example.test.test.annotation;

import java.lang.annotation.*;

/**
 * 在需要复制的类上加上该注解可以使用 {@link CopyUtils}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Copy {
    /**
     * 是否复制null
     * @return
     */
    boolean copyNull() default false;
}
