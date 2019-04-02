package com.example.test.test.annotation;

import java.lang.annotation.*;

/**
 * @description 在需要复制的类上加上该注解可以使用 {@link CopyUtils}
 * @author beiluo
 * @date 9:36 2019/4/2
 * @version V1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Copy {
    /**
     * @description 是否复制null
     * @author beiluo
     * @date 9:36 2019/4/2
     * @return boolean
     * @param
     * @version V1.0
     */
    boolean copyNull() default false;
}
