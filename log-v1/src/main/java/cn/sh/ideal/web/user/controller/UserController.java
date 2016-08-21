package cn.sh.ideal.web.user.controller;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.ideal.common.Pagination;
import cn.sh.ideal.demo.model.FooBean;
import cn.sh.ideal.servlet.annotation.FastJson;
import cn.sh.ideal.servlet.converter.JsonResultData;
import cn.sh.ideal.servlet.mvc.BaseController;
import cn.sh.ideal.servlet.resolver.JSONObjectWrapper;
import cn.sh.ideal.web.user.model.User;
import cn.sh.ideal.web.user.service.UserService;

import com.github.pagehelper.PageHelper;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	@Autowired
	UserService userService;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping("/allUsers")
	@ResponseBody
	public JsonResultData getUsers() throws Exception {
		// int i = 1/0;
		PageHelper.startPage(1, 2);
		return new JsonResultData(cn.sh.ideal.servlet.HttpStatus.CODE_200, Pagination.build(userService.getUsers()));
	}

	@RequestMapping(value = "/fastjson", method = RequestMethod.POST)
	public @ResponseBody FooBean fastjson(@FastJson FooBean foo) {
		System.out.println(foo);
		return foo;
	}

	@RequestMapping(value = "/fastjson1", method = RequestMethod.POST)
	public @ResponseBody JSONObjectWrapper fastjson2(@FastJson JSONObjectWrapper foo) {
		System.out.println(foo);
		return foo;
	}

	@RequestMapping(value = "/val")
	@ResponseBody
	public User validate(@Valid UserBean userBean) {
		String msg = messageSource.getMessage("sample.age.not.valid", null, null);
		User user = new User();
		user.setPassword(msg);
		user.setUsername(userBean.getUsername());
		return user;
	}

	public static class UserBean {
		@NotEmpty
		private String username;
		@NotEmpty
		private String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}
}
