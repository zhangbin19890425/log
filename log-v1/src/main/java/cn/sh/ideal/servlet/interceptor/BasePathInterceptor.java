package cn.sh.ideal.servlet.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class BasePathInterceptor extends HandlerInterceptorAdapter {
	protected String basePathName = "BASE_PATH";
	protected String basePath;

	public void setBasePathName(String basePathName) {
		this.basePathName = basePathName;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		request.setAttribute(basePathName, getBasePath(request));
	}

	protected String getBasePath(HttpServletRequest request) {
		if (basePath == null) {
			String path = request.getContextPath();
			basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		}
		return basePath;
	}
}
