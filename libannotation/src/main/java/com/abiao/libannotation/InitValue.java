package com.abiao.libannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * create by dengwei.yu.o
 * At 2020/12/9 1:10 PM
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InitValue {

    String value() default "阿彪好帅";
}
