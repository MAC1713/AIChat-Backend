package com.ai.aichatbackend.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mac
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoFetchPrompt {

    // 指定promptId的字段名
    String promptIdField() default "";

    // 字段类型,默认为string
    String type() default "string";
}
