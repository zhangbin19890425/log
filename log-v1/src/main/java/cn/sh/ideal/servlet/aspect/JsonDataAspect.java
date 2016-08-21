package cn.sh.ideal.servlet.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import cn.sh.ideal.servlet.HttpStatus;
import cn.sh.ideal.servlet.converter.JsonResultData;

@Aspect
public class JsonDataAspect extends BaseAspect {

	@Override
	public void setDebug(boolean debug) {
		super.setDebug(debug);
	}

	//@Around("@annotation(org.springframework.web.bind.annotation.ResponseBody)")
	public Object doAround(ProceedingJoinPoint point) throws Throwable {
		Object retVal = null;
		try {
			retVal = point.proceed();
		} catch (Throwable e) {
			try {
				if (JsonResultData.class == getMethod(point).getReturnType()) {//
					if (debug) {
						System.out.println(e);
					}
					return JsonResultData.wrapper(HttpStatus.CODE_500, null, "数据请求错误：" + e.getMessage());
				}
				// 抛出非处理异常
				throw e;
			} catch (Throwable e1) {
				// ignoge Throwable
			}
		}

		return retVal;
	}

	@Around("@annotation(cn.sh.ideal.servlet.annotation.ResponseJson)")
	public Object doAroundResponseJson(ProceedingJoinPoint point) throws Throwable {
		Object retVal = null;
		String code = HttpStatus.CODE_200;
		String description = null;
		try {
			retVal = point.proceed();
		} catch (Throwable e) {
			code = HttpStatus.CODE_500;
			description = "数据请求错误：" + e.getMessage();
			if (debug) {
				System.out.println(e);
			}
		}
		Class<?> returnType = getMethod(point).getReturnType();
		if (JsonResultData.class == returnType) {//
			if (retVal == null) {
				retVal = JsonResultData.wrapper(code, null, description);
			}
		} else if (Object.class == returnType) {//
			retVal = JsonResultData.wrapper(code, retVal, description);
		}
		return retVal;
	}
}
