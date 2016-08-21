package cn.sh.ideal.servlet.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public abstract class BaseAspect {

	protected boolean debug;

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Get value of annotated method parameter
	 */
	protected Method getMethod(ProceedingJoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		return methodSignature.getMethod();
	}
	
}
