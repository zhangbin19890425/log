package cn.sh.ideal.servlet.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;

@SuppressWarnings({ "serial" })
public class DispatcherServlet extends org.springframework.web.servlet.DispatcherServlet {

	/**
	 * 包装 HttpServletRequest
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!(request instanceof HttpServletRequestInterceptor))
			request = HttpServletRequestInterceptor.wrapperInstance(request);
		super.service(request, response);
	}

	/**
	 * @ClassName: HttpServletRequest 拦截器用于扩展 Interceptor
	 * @author: Administrator
	 * @date: 2015年11月11日 下午1:21:13
	 */
	public static class HttpServletRequestInterceptor extends HttpServletRequestWrapper {
		final HttpServletRequest target;

		public static HttpServletRequest wrapperInstance(HttpServletRequest request) {
			return new HttpServletRequestInterceptor(request);
		}

		public HttpServletRequestInterceptor(HttpServletRequest target) {
			super(target);
			this.target = target;
		}

		public HttpServletRequest getTarget() {
			return this.target;
		}

		public void setThis(HttpServletRequest _this) {
			Assert.notNull(_this, "Request cannot be null");
			super.setRequest(_this);
		}

		public HttpServletRequest getThis() {
			return (HttpServletRequest) super.getRequest();
		}
	}

	/**
	 * @ClassName: BaseHttpServletRequest 用于 Wrapper
	 * @author: Administrator
	 * @date: 2015年12月9日 上午9:32:39
	 */
	public static class BaseHttpServletRequest extends HttpServletRequestWrapper {

		public BaseHttpServletRequest(HttpServletRequestInterceptor request) {
			super(request.getThis());
			request.setThis(this);
		}
	}
}
