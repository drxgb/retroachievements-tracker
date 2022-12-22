package com.drxgb.ratracker.util.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to map JSON settings object to an entity.
 * @author Dr.XGB
 * @version 1.0.0
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@Repeatable(FieldKeyGroup.class)
public @interface FieldKey
{
	String name() default "";
	boolean count() default false;
}
