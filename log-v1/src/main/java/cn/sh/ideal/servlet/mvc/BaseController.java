package cn.sh.ideal.servlet.mvc;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseController {
	protected static final Logger logger = Logger.getLogger(BaseController.class);

	@ExceptionHandler
	public String handleException(Exception e, HttpServletRequest request) {
		logger.error("接口请求错误", e);
		request.setAttribute("error", e.getMessage());

		return "common/error";
	}
}
