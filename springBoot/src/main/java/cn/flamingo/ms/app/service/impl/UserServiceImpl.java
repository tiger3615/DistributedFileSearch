package cn.flamingo.ms.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.flamingo.ms.app.facade.model.User;
import cn.flamingo.ms.app.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
	private static final Logger log = LoggerFactory.getLogger("INFO");

	@Override
	public List<User> selectAll() {
		List<User> list = new ArrayList<>();
		list.add(new User());
		list.add(new User());
		return list;
	}

	@Override
	public boolean login(User user) {
		log.info("" + user);
		return true;
	}

}
