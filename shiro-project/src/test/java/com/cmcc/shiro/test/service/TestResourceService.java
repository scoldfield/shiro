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
        res.setName("ϵͳ����");
        res.setUrl("/admin/*");
        resourceService.add(res);
        
        res = new Resource();
        res.setName("�û�����");
        res.setUrl("/admin/user/*");
        resourceService.add(res);
        
        res = new Resource();
        res.setName("�û����");
        res.setUrl("/admin/user/add");
        resourceService.add(res);
        
        res = new Resource();
        res.setName("�û�ɾ��");
        res.setUrl("/admin/user/delete");
        resourceService.add(res);
        
        res = new Resource();
        res.setName("��ɫ����");
        res.setUrl("/admin/role/*");
        resourceService.add(res);
        
        res = new Resource();
        res.setName("��ɫ���");
        res.setUrl("/admin/role/add");
        resourceService.add(res);
        
        res = new Resource();
        res.setName("��ɫ�޸�");
        res.setUrl("/admin/role/update");
        resourceService.add(res);
	}
}
