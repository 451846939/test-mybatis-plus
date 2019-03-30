package com.example.test.test.annotation;


import java.lang.annotation.*;

/**
 * @author beiluo
 * Email beiluo@uoko.com
 * @version V1.0
 * @description
 * @date 2019/3/29 14:40
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CopyField {
    /**
     * @description {@link CopyField#copyFieldName()} 若copyFieldName和该字段都未设置那么默认读取同样名字的字段
     * @author beiluo
     * @date 14:44 2019/3/29
     * @return java.lang.String
     * @param
     * @version V1.0
     */
    String value() default "";
    /**
     * @description 需要复制的对象中的字段名称
     * @author beiluo
     * @date 14:57 2019/3/29
     * @return java.lang.String
     * @param
     * @version V1.0
     */
    String copyFieldName() default "";
    /**
     * @description 分组，根据不同的分组复制不同分组下的copyFieldName() {@link CopyField#copyFieldName()}
     * 该class必须为映射的类的class
     * @author beiluo
     * @date 15:00 2019/3/29
     * @return java.lang.Class<?>[]
     * @param
     * @version V1.0
     */
    Class<?> group() default Class.class;

    /**
     * 是否需要复制
     * @return
     */
    boolean copy() default true;
}
