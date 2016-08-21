package cn.sh.ideal.servlet.interceptor;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;

import cn.sh.ideal.servlet.annotation.RequestCharset;
import cn.sh.ideal.servlet.mvc.DispatcherServlet.BaseHttpServletRequest;
import cn.sh.ideal.servlet.mvc.DispatcherServlet.HttpServletRequestInterceptor;


public class CharsetHandleInterceptor extends AbstractHandlerInterceptor {
	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());
	private String requestCharsetName = "ISO-8859-1";
	private String encoding = "UTF-8";
	private Resource configLocation;
	private boolean isSameCharset = false;// 相同的 编码(默认不相同)

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void setRequestCharsetName(String requestCharsetName) {
		this.requestCharsetName = requestCharsetName;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.configLocation != null) {
			Properties config = new Properties();
			config.load(this.configLocation.getInputStream());
			this.requestCharsetName = config.getProperty("requestCharsetName");
			this.encoding = config.getProperty("encoding");
		}

		Assert.notNull(this.requestCharsetName, "requestCharsetName cannot be null");
		Assert.notNull(this.encoding, "encoding cannot be null");
		checkCharset("encoding", this.encoding);
		checkCharset("requestCharsetName", this.requestCharsetName);
		this.isSameCharset = this.encoding.equalsIgnoreCase(this.requestCharsetName);

		logger.info("load charset:{'encoding':'" + this.encoding + "','requestCharsetName':'" + this.requestCharsetName + "'}");
	}

	@Override
	protected void handleInternal(HttpServletRequestInterceptor request, HttpServletResponse response, HandlerMethod handler) {
		if (isSameCharset) return;
		RequestCharset requestCharset = handler.getMethodAnnotation(RequestCharset.class);
		if (requestCharset == null) return;
		new CharsetHttpServletRequest(request, this.encoding, this.requestCharsetName);
	}

	protected void handleInternal(HttpServletRequestInterceptor request, HandlerMethod handler) {
		RequestCharset requestCharset = handler.getMethodAnnotation(RequestCharset.class);
		if (requestCharset == null) return;
		new CharsetHttpServletRequest(request, this.encoding, this.requestCharsetName);
	}

	/**
	 * @Title: 校验 字符编码
	 * @param propertyName
	 * @param charsetName
	 * @throws UnsupportedCharsetException void
	 */
	private void checkCharset(String propertyName, String charsetName) throws UnsupportedCharsetException {
		try {
			Charset.forName(charsetName);
		} catch (UnsupportedCharsetException e) {
			logger.error("Failed convert " + propertyName + ":[" + charsetName + "]", e);
			throw e;
		}
	}

	/**
	 * @ClassName: CharsetHttpServletRequestWrapper
	 * @author: Administrator
	 * @date: 2015年11月11日 下午1:21:13
	 */
	private static class CharsetHttpServletRequest extends BaseHttpServletRequest {
		private String encoding;
		private String requestCharsetName;

		public CharsetHttpServletRequest(HttpServletRequestInterceptor request, String encoding, String requestCharsetName) {
			super(request);
			this.encoding = encoding;
			this.requestCharsetName = requestCharsetName;
		}

		@Override
		public String getParameter(String name) {
			String parameter = super.getParameter(name);
			return convert(parameter);
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
			if (parameterValues == null) return null;
			for (int i = 0, length = parameterValues.length; i < length; i++) {
				parameterValues[i] = this.convert(parameterValues[i]);
			}
			return parameterValues;
		}

		/**
		 * @Title: 字符编码转换
		 * @param target
		 * @return String
		 */
		private String convert(String target) {
			try {
				if (target == null) return null;
				return new String(target.getBytes(requestCharsetName), encoding);
			} catch (Exception e) {
				return target;
			}
		}
	}

}
