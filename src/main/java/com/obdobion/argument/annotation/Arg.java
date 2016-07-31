package com.obdobion.argument.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(Args.class)
public @interface Arg {

    boolean allowCamelCaps() default false;

    boolean allowMetaphone() default false;

    boolean caseSensitive() default false;

    String[] defaultValues() default {};

    String[] excludeArgs() default {};

    String factoryArgName() default "";

    String factoryMethod() default "";

    String format() default "";

    String help() default "";

    String[] inList() default {};

    String instanceClass() default "";

    String longName() default "";

    String matches() default "";

    int multimax() default Integer.MAX_VALUE;

    int multimin() default 1;

    boolean positional() default false;

    String[] range() default {};

    boolean required() default false;

    char shortName() default ' ';

    int uniqueId() default 0;
}
