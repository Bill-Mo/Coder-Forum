package com.coder.community;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.coder.community.controller.HomeController;
import com.coder.community.dao.alphaDao;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CommunityApplicationTests implements ApplicationContextAware{

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;

	}

	@Test
	public void testDummy() {
		System.out.println("abc");
	}
	
	@Test
	public void contextLoads() {
		// System.out.println(applicationContext);
		alphaDao dao = applicationContext.getBean("normal", alphaDao.class);
		// System.out.println(dao.select());

		// alphaService service = applicationContext.getBean(alphaService.class);
		// System.out.println(service);
		// service = applicationContext.getBean(alphaService.class);
		// System.out.println(service);

		HomeController homeController = applicationContext.getBean(HomeController.class);
	}

}
