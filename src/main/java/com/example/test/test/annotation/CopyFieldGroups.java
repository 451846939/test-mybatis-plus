package com.example.test.test.annotation;

import java.lang.annotation.*;

/**
 * @description {@link CopyField} 的组
 * @author beiluo
 * @date 9:35 2019/4/2
 * @version V1.0
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
