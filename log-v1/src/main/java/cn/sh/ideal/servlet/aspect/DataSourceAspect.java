package cn.sh.ideal.servlet.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import cn.sh.ideal.jdbc.datasource.router.DynamicDataSourceHolder;
import cn.sh.ideal.servlet.annotation.DataSource;

@Aspect
public class DataSourceAspect {

	@Before("execution(* cn.sh.ideal.*.service.*.*(..))")
	public void doBefore(JoinPoint point) {

		Object target = point.getTarget();
		String method = point.getSignature().getName();

		Class<?>[] classz = target.getClass().getInterfaces();

		Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
		try {
			Method m = classz[0].getMethod(method, parameterTypes);
			if (m != null && m.isAnnotationPresent(DataSource.class)) {
				DataSource data = m.getAnnotation(DataSource.class);
				// DynamicDataSourceHolder.clear();
				// System.out.println("threadyy = "+Thread.currentThread().getId());
				DynamicDataSourceHolder.putDataSource(data.value());
				System.out.println(method + "-----------------> " + data.value());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
