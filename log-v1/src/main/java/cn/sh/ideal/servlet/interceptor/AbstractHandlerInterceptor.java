package cn.sh.ideal.servlet.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.sh.ideal.servlet.mvc.DispatcherServlet.HttpServletRequestInterceptor;


public abstract class AbstractHandlerInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (request instanceof HttpServletRequestInterceptor) {
			if (handler instanceof HandlerMethod) {
				this.handleInternal((HttpServletRequestInterceptor) request, response, (HandlerMethod) handler);
			}
			this.handleInternal((HttpServletRequestInterceptor) request, response, handler);
		}
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	protected void handleInternal(HttpServletRequestInterceptor request, HttpServletResponse response, Object handler) {

	}

	protected void handleInternal(HttpServletRequestInterceptor request, HttpServletResponse response, HandlerMethod handler) {
		
	}
}
