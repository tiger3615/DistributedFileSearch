package cn.flamingo.ms.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@GetMapping("/list")
	public String helloworld() {
		return "user list";
	}
}
