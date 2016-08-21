package cn.sh.ideal.servlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestFilter {
	String value() default "";

	boolean filterEmpty() default false;

	public static final String LTRIM_REG = "^\\s*";// 去除左空格
	public static final String RTRIM_REG = "\\s*$";// 去除右空格
	public static final String TRIM_REG = "(" + LTRIM_REG + ")|(" + RTRIM_REG + ")";// 去除左右空格
}