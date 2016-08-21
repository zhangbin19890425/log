package cn.sh.ideal.servlet.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;

import cn.sh.ideal.servlet.annotation.RequestFilter;
import cn.sh.ideal.servlet.mvc.DispatcherServlet.BaseHttpServletRequest;
import cn.sh.ideal.servlet.mvc.DispatcherServlet.HttpServletRequestInterceptor;

public class FilterHandleInterceptor extends AbstractHandlerInterceptor {
	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	@Override
	protected void handleInternal(HttpServletRequestInterceptor request, HttpServletResponse response, HandlerMethod handler) {
		RequestFilter requestFilter = handler.getMethodAnnotation(RequestFilter.class);
		if (requestFilter == null) return;
		new FilterHttpServletRequestWrapper(request, requestFilter.value(), requestFilter.filterEmpty());
	}

	/**
	 * @ClassName: CharsetHttpServletRequestWrapper
	 * @author: Administrator
	 * @date: 2015年11月11日 下午1:21:13
	 */
	private static class FilterHttpServletRequestWrapper extends BaseHttpServletRequest {
		private String reg;
		private boolean filterEmpty = false;

		public FilterHttpServletRequestWrapper(HttpServletRequestInterceptor request, String reg, boolean filterEmpty) {
			super(request);
			this.reg = reg;
			this.filterEmpty = filterEmpty;
		}

		@Override
		public String getParameter(String name) {
			String parameter = super.getParameter(name);
			return filter(parameter);
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			Map<String, String[]> parameterMap = new HashMap<String, String[]>();
			Enumeration<String> enumeration = super.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String name = enumeration.nextElement();
				parameterMap.put(name, this.getParameterValues(name));
			}
			return parameterMap;
		}

		@Override
		public String[] getParameterValues(String name) {
			String[] parameterValues = super.getParameterValues(name);
			if (parameterValues == null || parameterValues.length == 0) return null;
			for (int i = 0, length = parameterValues.length; i < length; i++) {
				parameterValues[i] = this.filter(parameterValues[i]);
			}
			return parameterValues;
		}

		public String filter(String parameter) {
			if (parameter == null || (this.filterEmpty && parameter.trim().isEmpty())) return null;
			return parameter.replaceAll(reg, "");
		}
	}
}
