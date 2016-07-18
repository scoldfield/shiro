package com.cmcc.shiro.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cmcc.basic.dao.BaseDao;
import com.cmcc.shiro.model.Resource;
import com.cmcc.shiro.model.Role;
import com.cmcc.shiro.model.User;

@Repository("userDao")
public class UserDao extends BaseDao<User> implements IUserDao {


	public List<User> listUser() {
		return super.list("from User");
	}
	
	public User loadByUsername(String username) {
		return (User)super.queryObject("from User where username=?", username);
	}

	public List<User> listByRole(int id) {
		String hql = "select u from User u,Role r,UserRole ur where u.id=ur.userId and r.id=ur.roleId and r.id=?";
//		return super.listObj(hql, id);
	      return null;

	}

	public List<Resource> listAllResource(int uid) {
		String hql = "select res from User u,Resource res,UserRole ur,RoleResource rr where " +
				"u.id=ur.userId and ur.roleId=rr.roleId  and rr.resId=res.id and u.id=?";
//		return super.listObj(hql, uid);
	      return null;

	}

	@Override
	public List<String> listRoleSnByUser(int uid) {
		String hql = "select r.sn from UserRole ur,Role r,User u where u.id=ur.userId and r.id=ur.roleId and u.id=?";
//		return super.listObj(hql, uid);
	      return null;

	}
	
	@Override
	public List<Role> listUserRole(int uid) {
		String hql = "select r from UserRole ur,Role r,User u where u.id=ur.userId and r.id=ur.roleId and u.id=?";
//		return super.listObj(hql, uid);
	      return null;

	}

}
