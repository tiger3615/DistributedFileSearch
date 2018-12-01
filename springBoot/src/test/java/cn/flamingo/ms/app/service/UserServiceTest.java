package cn.flamingo.ms.app.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.flamingo.ms.app.App;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { App.class })
public class UserServiceTest {
	private static final Logger log = LoggerFactory.getLogger("TEST");
	
	@Test
	public void testOne() {
		log.info("test user 1");
	}
}
