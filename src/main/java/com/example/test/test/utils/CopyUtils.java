package com.example.test.test.utils;

import com.example.test.test.annotation.Copy;
import com.example.test.test.annotation.CopyField;
import com.example.test.test.annotation.CopyFieldGroups;
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
    @Deprecated
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
        return copyObjNotNull(t,clazz);
    }

    /**
     * 根据 {@link Copy,CopyField,CopyFieldGroups}注解进行复制对象
     * @param copyObj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Object copyObjOnAnnotation(T copyObj,Class<?> clazz){
        Object aClass = null;
        try {
            if (copyObj == null) {
                return null;
            }
            aClass = clazz.getDeclaredConstructor().newInstance();
            copyObjOnAnnotation(copyObj,aClass);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("copy err " + clazz.getClass(), e);
        }
        return aClass;
    }

    /**
     * 根据注解复制
     * @param copyObj
     * @param copyOnObj
     * @param <T>
     * @param <M>
     * @return
     */
    public static <T, M> M copyObjOnAnnotation(T copyObj,M copyOnObj) {
        Copy copy = copyOnObj.getClass().getDeclaredAnnotation(Copy.class);
        String[] notCopyFieldNames=null;
        Field[] fieldsWithAnnotation = FieldUtils.getFieldsWithAnnotation(copyOnObj.getClass(), CopyField.class);
        if (copy !=null){
            if (fieldsWithAnnotation!=null&&fieldsWithAnnotation.length!=0){
                 notCopyFieldNames = Arrays.stream(fieldsWithAnnotation).map(field -> {
                    CopyField declaredAnnotation = field.getDeclaredAnnotation(CopyField.class);
                    if (!declaredAnnotation.copy()){
                        return field.getName();
                    }else {
                        return null;
                    }
                }).filter(Objects::nonNull).toArray(String[]::new);
                copyObj(copyObj,copyOnObj,copy.copyNull(),true,notCopyFieldNames,null);
            }else if (notCopyFieldNames!=null&&notCopyFieldNames.length!=0){
                copyObj(copyObj,copyOnObj,copy.copyNull(),true,notCopyFieldNames,null);
            }else {
                copyObj(copyObj,copyOnObj,copy.copyNull(),true,null,null);
            }
        }
        return copyOnObj;
    }

    public static <T> Object copyObj(final T t, final Class clazz,final String... filedName) {
        return copyObj(t,clazz,true,null,filedName);
    }

    public static <T> Object copyObj(final T t,final Class clazz,Boolean copyNull,final String... filedName) {
       return copyObj(t,clazz,copyNull,null,filedName);
    }

    public static <T> Object copyObjNotNull(final T t,final Class clazz,final String... filedName) {
        return copyObj(t,clazz,false,null,filedName);
    }

    /**
     * 复制对象
     * @param t
     * @param clazz
     * @param copyNull
     * @param notCopyFiledName
     * @param filedName
     * @param <T>
     * @return
     */
    public static <T> Object copyObj(final T t,final Class clazz,final Boolean copyNull,final String[] notCopyFiledName,final String... filedName) {
        Object aClass = null;
        try {
            if (t == null) {
                return null;
            }
            aClass = clazz.getDeclaredConstructor().newInstance();
            copyObj(t,aClass,copyNull,false,notCopyFiledName,filedName);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("copy err " + clazz.getClass(), e);
        }
        return aClass;
    }

    /**
     * 复制不为null的对象
     * @param t
     * @param copyOnObj
     * @param notCopyFiledName
     * @param filedName
     * @param <T>
     * @param <M>
     * @return
     */
    public static <T, M> M copyObjNotNull(final T t,final M copyOnObj,final String[] notCopyFiledName,final String... filedName) {
            if (t == null) {
                return null;
            }
        return copyObj(t,copyOnObj,false,false,notCopyFiledName,filedName);
    }

    /**
     * 复制不为null的对象
     * @param t
     * @param copyOnObj
     * @param filedName
     * @param <T>
     * @param <M>
     * @return
     */
    public static <T, M> M copyObjNotNull(final T t,final M copyOnObj,final String... filedName) {
        if (t == null) {
            return null;
        }
        return copyObj(t,copyOnObj,false,true,null,filedName);
    }

    /**
     * 复制对象
     * @param copyObj 被复制的对象
     * @param copyOnObj 复制的对象
     * @param copyNull 是否复制null属性
     * @param annotationCopy 是否使用配置的注解复制
     * @param notCopyFiledName 不复制的字段
     * @param copyFiledName 复制的字段
     * @param <T>
     * @param <M>
     * @return
     */
    public static <T, M> M copyObj(final T copyObj,final M copyOnObj,final Boolean copyNull,boolean annotationCopy,final String[] notCopyFiledName,final String[] copyFiledName) {
        try {
            Field[] tFields = FieldUtils.getAllFields(copyObj.getClass());
            Field[] asmFields = FieldUtils.getAllFields(copyOnObj.getClass());
            if (tFields!=null&&asmFields!=null&&tFields.length != 0 && asmFields.length != 0) {
                 Arrays.stream(tFields)
                        //筛选出需要复制的字段名字
                        .filter(field -> needCopyFiledNameFilter(copyFiledName, field))
                        //去除不复制的字段
                        .filter(field -> notCopyFiledNameFilter(notCopyFiledName, field))
                        //去除为null或者""的string 的对象，去除不要复制的字段
                        .filter(field -> nullFilter(copyObj, copyNull, field))
                        //循环所有的筛选出来的复制的对象的字段`
                        .forEach(copyObjFiled-> Arrays.stream(asmFields).forEach(copyOnObjFiled->{
                            // 注解的判断
                            Copy copy = copyOnObj.getClass().getDeclaredAnnotation(Copy.class);
                            if (annotationCopy&& copy !=null){
                                copyFieldByAnnotation(copyObj, copyOnObj, copyObjFiled, copyOnObjFiled);
                            }else {
                                if (copyObjFiled.getName().equals(copyOnObjFiled.getName())){
                                    copy0(copyObj, copyOnObj, copyObjFiled, copyOnObjFiled);
                                }
                            }

                        }));
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not copy ", e);
        }
        return copyOnObj;
    }

    private static <T, M> void copyFieldByAnnotation(final T copyObj,final M copyOnObj,final Field copyObjFiled,final Field copyOnObjFiled) {
        CopyFieldGroups groups = copyOnObjFiled.getAnnotation(CopyFieldGroups.class);
        if (groups!=null){
            CopyField[] copyFields = groups.groups();
            if (copyFields.length!=0){
                Class<?> copyOnObjClass = copyObj.getClass();
                Arrays.stream(copyFields).filter(CopyField::copy).forEach(copyField -> {
                    Class<?> group = copyField.group();
                    if (group!=null&&copyOnObjClass.equals(group)){
                        copyFieldByAnnotation0(copyObj, copyOnObj, copyObjFiled, copyOnObjFiled,copyField);
                    }
                });
            }else {
                throw new RuntimeException("CopyFieldGroups need CopyField");
            }
        }else {
            CopyField copyField = copyObjFiled.getAnnotation(CopyField.class);
            copyFieldByAnnotation0(copyObj, copyOnObj, copyObjFiled, copyOnObjFiled,copyField);
        }

    }

    private static <T, M> void copyFieldByAnnotation0(T copyObj, M copyOnObj, Field copyObjFiled, Field copyOnObjFiled,CopyField copyField) {
        if (copyField!=null&&!copyField.copy()){
            return;
        }
        if (copyField!=null){
            //如果注解的value和copyFieldName属性不为null那么根据这两个值去复制
            if ((StringUtils.isNotEmpty(copyField.value())&&copyObjFiled.getName().equals(copyField.value()))||(StringUtils.isNotEmpty(copyField.copyFieldName())&&copyObjFiled.getName().equals(copyField.copyFieldName()))){
                copy0(copyObj, copyOnObj, copyObjFiled, copyOnObjFiled);
            }else {
                //如果没有这两个属性那么根据字段名字去复制
                if (copyObjFiled.getName().equals(copyOnObjFiled.getName())){
                    copy0(copyObj, copyOnObj, copyObjFiled, copyOnObjFiled);
                }
            }
        }else {
            //没有加注解，按照默认名字去复制
            if (copyObjFiled.getName().equals(copyOnObjFiled.getName())){
                copy0(copyObj, copyOnObj, copyObjFiled, copyOnObjFiled);
            }
        }
    }

    /**
     * 空字段的过滤字符串格式为""或者null表示为空
     * @param copyObj
     * @param copyNull
     * @param field
     * @param <T>
     * @return
     */
    private static <T> boolean nullFilter(T copyObj, Boolean copyNull, Field field) {
        try {
            field.setAccessible(true);
            return copyNull ||
                    filedIsNotNull(copyObj, field);
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    /**
     * 不需要复制的字段的过滤
     * @param notCopyFiledName
     * @param field
     * @return
     */
    private static boolean notCopyFiledNameFilter(String[] notCopyFiledName, Field field) {
        if (notCopyFiledName==null){
            return true;
        }else if (notCopyFiledName!=null&&notCopyFiledName.length==0){
            return true;
        }else {
            return !hasNotCopyFiledName(notCopyFiledName, field);
        }
    }

    /**
     * 需要复制的字段的过滤
     * @param copyFiledName
     * @param field
     * @return
     */
    private static boolean needCopyFiledNameFilter(String[] copyFiledName, Field field) {
        if (copyFiledName!=null&&copyFiledName.length==0){
            return true;
        }else if (copyFiledName==null) {
            return true;
        }else {
            return hasCopyFiledName(copyFiledName, field);
        }
    }

    /**
     * 是否含有需要复制的字段
     * @param copyFiledName
     * @param field
     * @return
     */
    private static boolean hasCopyFiledName(String[] copyFiledName, Field field) {
        return Arrays.stream(copyFiledName).anyMatch(needCopyFiled -> field.getName().equals(needCopyFiled));
    }

    /**
     * 复制字段
     * @param copyObj
     * @param copyOnObj
     * @param copyObjFiled
     * @param copyOnObjFiled
     * @param <T>
     * @param <M>
     */
    private static <T, M> void copy0(final T copyObj,final M copyOnObj, Field copyObjFiled, Field copyOnObjFiled) {
        try {
            copyObjFiled.setAccessible(true);
            copyOnObjFiled.setAccessible(true);
            Object o = copyObjFiled.get(copyObj);
            copyOnObjFiled.set(copyOnObj,o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("copy err " + copyObj.getClass(), e);
        }
    }

    /**
     * 字段是否为空
     * @param copyObj
     * @param field
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    private static <T> boolean filedIsNotNull(T copyObj, Field field) throws IllegalAccessException {
        if (field.getType().equals(String.class)){
            return StringUtils.isNotEmpty((String)field.get(copyObj));
        }else {
            return !Objects.isNull(field.get(copyObj));
        }

    }

    /**
     * 是否含有不需要复制的字段
     * @param notCopyFiledName
     * @param field
     * @return
     */
    private static boolean hasNotCopyFiledName(String[] notCopyFiledName, Field field) {
        return Arrays.stream(notCopyFiledName).anyMatch(netCopyFiled-> netCopyFiled.equals(field.getName()));
    }
}
