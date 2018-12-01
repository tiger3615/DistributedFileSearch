package cn.flamingo.ms.app.service;

import java.util.List;

import cn.flamingo.ms.app.facade.model.User;

public interface UserService {
	public List<User> selectAll();
	public boolean login(User user);
}
