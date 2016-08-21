package cn.sh.ideal.web.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sh.ideal.web.user.mapper.UserMapper;
import cn.sh.ideal.web.user.model.User;
import cn.sh.ideal.web.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	//@Autowired
	UserMapper userMapper;

	@Override
	public List<User> getUsers() throws Exception {
		return userMapper.getUsers();
	}

}
