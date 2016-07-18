package com.cmcc.shiro.service;

import java.util.List;

import com.cmcc.shiro.model.Resource;
import com.cmcc.shiro.model.Role;
import com.cmcc.shiro.model.User;


public interface IUserService {
	public void add(User user);
	
	public void add(User user,List<Integer> rids);
	
	public void delete(int id);
	
	public void update(User user,List<Integer> rids);
	
	public void update(User user);
	
	public User load(int id);
	
	public User loadByUsername(String username);
	
	public User login(String username,String password);
	
	public List<User> list();
	
	public List<User> listByRole(int id);
	
	public List<Resource> listAllResource(int uid);
	
	public List<String> listRoleSnByUser(int uid);
	
	public List<Role> listUserRole(int uid);
}
