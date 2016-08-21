package cn.sh.ideal.web.user.service;

import java.util.List;

import cn.sh.ideal.web.user.model.User;

public interface UserService {
	List<User> getUsers() throws Exception;
}
