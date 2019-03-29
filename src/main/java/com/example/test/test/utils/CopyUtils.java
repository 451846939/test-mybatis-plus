package com.example.test.test.utils;

import com.example.test.test.annotation.CopyField;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author beiluo
 * Email beiluo@uoko.com
 * @version V1.0
 * @description
 * @date 2019/3/5 15:44
 */
public class CopyUtils {
    /**
     * @param t 被复制的对象
     * @param m 复制到的对象
     * @return M
     * @description 复制对象包括父类字段，不复制null和""
     * @author beiluo
     * @date 15:47 2019/3/5
     * @version V1.0
     */
    public static <T, M> M copyObjNotNullAllFieldsAsObj(T t, M m) {
        try {
            Field[] tFields = FieldUtils.getAllFields(t.getClass());
            Field[] asmFields = FieldUtils.getAllFields(m.getClass());
            for (int i = 0, length = tFields.length; i < length; i++) {
                tFields[i].setAccessible(true);
                if (tFields[i].get(t) != null && !"".equals(tFields)) {
                    for (int j = 0, jlength = asmFields.length; j < jlength; j++) {
                        asmFields[j].setAccessible(true);
                        if (tFields[i].getName().equals(asmFields[j].getName())) {
                            if (tFields[i].getName().equals("serialVersionUID")) {
                                continue;
                            }
                            asmFields[j].set(m, tFields[i].get(t));
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not copy ", e);
        }
        return m;
    }

    /**
     * @param t     被复制的对象
     * @param clazz 生成的对象的Class
     * @return java.lang.Object
     * @description 根据指定Class复制对象
     * @author beiluo
     * @date 17:40 2019/3/5
     * @version V1.0
     */
    public static <T> Object copyObj(T t, Class clazz) {
        Object aClass = null;
        try {
            if (t == null) {
                return null;
            }
            aClass = clazz.getDeclaredConstructor().newInstance();
            copyObjNotNullAllFieldsAsObj(t, aClass);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("copy err " + clazz.getClass(), e);
        }
        return aClass;
    }

    public static <T, M> M copyObjOnAnnotation(T copyObj,M copyOnObj) {
        Field[] fieldsWithAnnotation = FieldUtils.getFieldsWithAnnotation(copyOnObj.getClass(), CopyField.class);
        Arrays.stream(fieldsWithAnnotation).map(field->{
            CopyField declaredAnnotation = field.getDeclaredAnnotation(CopyField.class);
            if (StringUtils.isNotEmpty(declaredAnnotation.value())){
                return declaredAnnotation.value();
            }else {
                if (StringUtils.isNotEmpty(declaredAnnotation.copyFieldName())){
                    return declaredAnnotation.copyFieldName();
                }
                return null;
            }
        });
                //todo
//        if (tFields!=null&&asmFields!=null&&tFields.length != 0 && asmFields.length != 0){
//            Arrays.stream(tFields)
//        }
//        return copyObj(t,clazz,true,null,filedName);
        return null;
    }

    public static <T> Object copyObj(T t, Class clazz, String... filedName) {
        return copyObj(t,clazz,true,null,filedName);
    }

    public static <T> Object copyObj(T t, Class clazz,Boolean copyNull, String... filedName) {
       return copyObj(t,clazz,copyNull,null,filedName);
    }

    public static <T> Object copyObj(T t, Class clazz,Boolean copyNull,String[] notCopyFiledName, String... filedName) {
        Object aClass = null;
        try {
            if (t == null) {
                return null;
            }
            aClass = clazz.getDeclaredConstructor().newInstance();
            copyObj(t,aClass,copyNull,notCopyFiledName,filedName);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("copy err " + clazz.getClass(), e);
        }
        return aClass;
    }


    public static <T, M> M copyObj(T copyObj, M copyOnObj, Boolean copyNull, String[] notCopyFiledName, String[] copyFiledName) {
        try {
            Field[] tFields = FieldUtils.getAllFields(copyObj.getClass());
            Field[] asmFields = FieldUtils.getAllFields(copyOnObj.getClass());
            if (tFields!=null&&asmFields!=null&&tFields.length != 0 && asmFields.length != 0) {
                Arrays.stream(tFields)
                        //筛选出需要复制的字段名字
                        .filter(field -> {
                    if (copyFiledName!=null&&copyFiledName.length!=0){
                        return Arrays.stream(copyFiledName).anyMatch(needCopyFiled->field.getName().equals(needCopyFiled));
                    }else {
                        return true;
                    }
                })
                    //去除为null或者""的string 的对象，去除不要复制的字段
                        .filter(field -> {
                    try {
                        field.setAccessible(true);
                        return copyNull||(
                                filedIsNotNull(copyObj, field) &&
                                        hasNotCopyFiledName(notCopyFiledName, field));
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                })
                        //循环所有的筛选出来的复制的对象的字段
                        .forEach(copyObjFiled-> Arrays.stream(asmFields).forEach(copyOnObjFiled->{
                            if (copyObjFiled.getName().equals(copyOnObjFiled.getName())){
                                copy0(copyObj, copyOnObj, copyObjFiled, copyOnObjFiled);
                            }else if (copyOnObjFiled.getDeclaredAnnotation(CopyField.class)!=null){
                                CopyField declaredAnnotation = copyOnObjFiled.getDeclaredAnnotation(CopyField.class);
                                //todo 如果是注解
                            }

                        }));
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not copy ", e);
        }
        return copyOnObj;
    }

    private static <T, M> void copy0(T copyObj, M copyOnObj, Field copyObjFiled, Field copyOnObjFiled) {
        try {
            copyObjFiled.setAccessible(true);
            Object o = copyObjFiled.get(copyObj);
            copyOnObjFiled.set(copyOnObj,o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("copy err " + copyObj.getClass(), e);
        }
    }

    private static <T> boolean filedIsNotNull(T copyObj, Field field) throws IllegalAccessException {
        if (field.getType().equals(String.class)){
            return StringUtils.isNotEmpty((String)field.get(copyObj));
        }else {
            return !Objects.isNull(field.get(copyObj));
        }

    }

    private static boolean hasNotCopyFiledName(String[] notCopyFiledName, Field field) {
        if (notCopyFiledName!=null&notCopyFiledName.length!=0){
            return Arrays.stream(notCopyFiledName).anyMatch(netCopyFiled-> netCopyFiled.equals(field));
        }
        return false;
    }
}
