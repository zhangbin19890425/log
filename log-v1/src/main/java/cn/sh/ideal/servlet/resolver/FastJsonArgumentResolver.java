package cn.sh.ideal.servlet.resolver;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import cn.sh.ideal.servlet.annotation.FastJson;

import com.alibaba.fastjson.JSON;

public class FastJsonArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(FastJson.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		// content-type不是json的不处理
		if (!request.getContentType().contains("application/json")) {
			return null;
		}

		// 把reqeust的body读取到StringBuilder
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();

		char[] buf = new char[1024];
		int rd;
		while ((rd = reader.read(buf)) != -1) {
			sb.append(buf, 0, rd);
		}

		/**
		 *  利用fastjson转换为对应的类型
		 *  
		 *  但是由于JSONObject实现了Map结果，所以Spring MVC3的默认处理器MapMethodProcessor会先起作用，
		 *  这样就不能正常的映射成JSONObject对象了。 没有办法做了一个简单的JSONObject包装类，
		 *  以使MapMethodProcessor不能对其进行处理。
		 */
		if (JSONObjectWrapper.class.isAssignableFrom(parameter.getParameterType())) {
			return new JSONObjectWrapper(JSON.parseObject(sb.toString()));
		} else {
			return JSON.parseObject(sb.toString(), parameter.getParameterType());
		}
	}

}
