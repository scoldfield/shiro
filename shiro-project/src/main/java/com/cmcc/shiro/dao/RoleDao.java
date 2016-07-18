package com.cmcc.shiro.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cmcc.basic.dao.BaseDao;
import com.cmcc.shiro.model.Resource;
import com.cmcc.shiro.model.Role;
import com.cmcc.shiro.model.RoleResource;
import com.cmcc.shiro.model.UserRole;

@Repository("roleDao")
public class RoleDao extends BaseDao<Role> implements IRoleDao {

	public List<Role> listRole() {
		return super.list("from Role");
	}

	public UserRole loadUserRole(int uid, int roleId) {
		String hql = "select ur from UserRole ur where ur.userId=? and ur.roleId=?";
//		return (UserRole)super.queryObject(hql,uid,roleId);
		return null;
	}

	public void addUserRole(int uid, int roleId) {
		UserRole ur = null;
		ur = loadUserRole(uid, roleId);
		if(ur==null) {
			ur = new UserRole();
			ur.setRoleId(roleId);
			ur.setUserId(uid);
			this.getSession().save(ur);
		}
	}

	public void deleteUserRole(int uid, int roleId) {
		UserRole ur = null;
		ur = loadUserRole(uid, roleId);
		if(ur!=null) {
			this.getSession().delete(ur);
		}
	}

	public void deleteUserRoles(int uid) {
		String hql = "delete UserRole ur where ur.userId=?";
		super.updateByHql(hql, uid);
	}

	public List<Resource> listRoleResource(int roleId) {
		String hql = "select res from Role role,Resource res,RoleResource rr where " +
				"role.id=rr.roleId and res.id=rr.resId and role.id=?";
		
//		return super.listObj(hql, roleId);
	      return null;

	}

	public void addRoleResource(int roleId, int resId) {
		RoleResource rr = null;
		rr = loadResourceRole(roleId, resId);
		if(rr==null) {
			rr = new RoleResource();
			rr.setResId(resId);
			rr.setRoleId(roleId);
			this.getSession().save(rr);
		}
	}

	public void deleteRoleResource(int roleId, int resId) {
		RoleResource rr = null;
		rr = loadResourceRole(roleId, resId);
		if(rr!=null) {
			this.getSession().delete(rr);
		}
	}

	public RoleResource loadResourceRole(int roleId, int resId) {
		String hql = "select rr from RoleResource rr where rr.roleId=? and rr.resId=?";
//		return (RoleResource)super.queryObject(hql, roleId,resId);
	      return null;

	}

}
