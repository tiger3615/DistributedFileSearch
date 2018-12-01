package cn.flamingo.ms.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.flamingo.ms.app.facade.model.User;
import cn.flamingo.ms.app.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@RequestMapping("/list")
	public List<User> selectAll() {		
		return userService.selectAll();
	}
}
