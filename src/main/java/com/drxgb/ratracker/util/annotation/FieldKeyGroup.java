package com.drxgb.ratracker.util.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used when the class member needs to have two or more field key
 * to be mapped.
 * @author Dr.XGB
 * @version 1.0.0
 * @see FieldKey
 */
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface FieldKeyGroup 
{
	FieldKey[] value();
}
