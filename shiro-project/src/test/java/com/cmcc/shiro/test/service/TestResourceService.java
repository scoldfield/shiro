package com.cmcc.shiro.test.service;

import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cmcc.shiro.model.Resource;
import com.cmcc.shiro.service.IResourceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/beans.xml")
public class TestResourceService {

	@Inject
	private SessionFactory sessionFactory;
	
	@Inject
	private IResourceService resourceService;
	
	@Test
	public void testAdd() {
		
	    Resource res = new Resource();
        res.setName("系统管理");
        res.setUrl("/admin/*");
        resourceService.add(res);
        
        res = new Resource();
        res.setName("用户管理");
        res.setUrl("/admin/user/*");
        resourceService.add(res);
        
        res = new Resource();
        res.setName("用户添加");
        res.setUrl("/admin/user/add");
        resourceService.add(res);
        
        res = new Resource();
        res.setName("用户删除");
        res.setUrl("/admin/user/delete");
        resourceService.add(res);
        
        res = new Resource();
        res.setName("角色管理");
        res.setUrl("/admin/role/*");
        resourceService.add(res);
        
        res = new Resource();
        res.setName("角色添加");
        res.setUrl("/admin/role/add");
        resourceService.add(res);
        
        res = new Resource();
        res.setName("角色修改");
        res.setUrl("/admin/role/update");
        resourceService.add(res);
	}
}
