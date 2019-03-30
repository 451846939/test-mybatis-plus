package com.example.test.test.annotation;

import java.lang.annotation.*;

/**
 * {@link CopyField} 的组
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CopyFieldGroups {
    /**
     * 如果有多个映射包含组
     * @return
     */
    CopyField[] groups() default {};
}
