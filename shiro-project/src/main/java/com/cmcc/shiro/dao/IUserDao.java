package com.cmcc.shiro.dao;

import java.util.List;

import com.cmcc.basic.dao.IBaseDao;
import com.cmcc.shiro.model.Resource;
import com.cmcc.shiro.model.Role;
import com.cmcc.shiro.model.User;


public interface IUserDao extends IBaseDao<User> {
	public List<User> listUser();
	
	public User loadByUsername(String username);
	
	public List<User> listByRole(int id);
	
	public List<Resource> listAllResource(int uid);
	
	public List<String> listRoleSnByUser(int uid);
	
	public List<Role> listUserRole(int uid);
	
}
