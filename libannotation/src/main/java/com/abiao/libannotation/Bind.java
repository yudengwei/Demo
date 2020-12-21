package com.abiao.libannotation;

import java.lang.reflect.Field;

/**
 * create by dengwei.yu.o
 * At 2020/12/9 1:12 PM
 **/
public class Bind {

    public static void bind(Object object) {
        try {
            Class<?> aClass = object.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                InitValue annotation = declaredField.getAnnotation(InitValue.class);
                declaredField.set(annotation.value(), object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
