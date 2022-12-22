package com.drxgb.ratracker.util.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

/**
 * Used to map controllers to a specified settings group.
 * @author Dr.XGB
 * @version 1.0.0
 */
@Retention(RUNTIME)
public @interface SettingsGroup
{
	String value();
}
