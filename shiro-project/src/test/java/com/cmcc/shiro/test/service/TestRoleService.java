package com.cmcc.shiro.test.service;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.cmcc.shiro.model.Role;
import com.cmcc.shiro.service.IRoleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class TestRoleService {

	@Inject
	private SessionFactory sessionFactory;
	
	@Inject
	private IRoleService roleService;
	
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
		Role r = new Role();
		r.setName("娴嬭瘯缁�");
		roleService.add(r);
	}
	
	@Test
	public void testAddUserRole() {
		roleService.addUserRole(1, 2);
		roleService.addUserRole(2, 1);
		roleService.addUserRole(3, 2);
		roleService.addUserRole(3, 3);
	}
	
	@Test
	public void testAddRoleRes() {
		roleService.addRoleResource(1, 1);
		roleService.addRoleResource(2, 2);
		roleService.addRoleResource(2, 3);
		roleService.addRoleResource(2, 4);
		
		roleService.addRoleResource(3, 5);
		roleService.addRoleResource(3, 6);
		roleService.addRoleResource(3, 7);
	}
	
	@Test
	public void testQuery() {
		
	}
}
