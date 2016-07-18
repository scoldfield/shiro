package com.cmcc.shiro.test.service;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.cmcc.shiro.model.User;
import com.cmcc.shiro.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class TestUserService {
	
	@Inject
	@Autowired
	private SessionFactory sessionFactory;
	
	@Inject
	@Autowired
	private IUserService userService;

	@Before
	public void setUp() {
	    //此时最好不要使用Spring的Transactional来管理，因为dbunit是通过jdbc来处理connection，再使用spring在一些编辑操作中会造成事务shisu
		Session s = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
		//SystemContext.setRealPath("D:\\teach_source\\class2\\j2EE\\dingan\\cms-da\\src\\main\\webapp");
	}
	
	@After
	public void tearDown() {
		SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
		Session s = holder.getSession(); 
		s.flush();
		TransactionSynchronizationManager.unbindResource(sessionFactory);
	}
	
	@Test
	public void testAdd() {
		User u = new User();
		u.setUsername("kh");
		u.setNickname("空号");
		u.setStatus(1);
		u.setPassword("111");
		userService.add(u);
	}
	
	@Test
	public void testRoleUser() {
		System.out.println(userService.listByRole(2));
	}
	
	@Test
	public void testUserRes() {
		System.out.println(userService.listAllResource(3));
	}
	
	
}
