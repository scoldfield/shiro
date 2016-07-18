package com.cmcc.shiro.dao;

import java.util.List;

import com.cmcc.basic.dao.IBaseDao;
import com.cmcc.shiro.model.Resource;
import com.cmcc.shiro.model.Role;
import com.cmcc.shiro.model.RoleResource;
import com.cmcc.shiro.model.UserRole;


public interface IRoleDao extends IBaseDao<Role>{
	public List<Role> listRole();
	
	public UserRole loadUserRole(int uid,int roleId);
	
	public void addUserRole(int uid,int roleId);
	
	public void deleteUserRole(int uid,int roleId);
	
	/**
	 * 鍒犻櫎鏌愪釜鐢ㄦ埛鐨勬墍鏈夎鑹�
	 * @param uid
	 */
	public void deleteUserRoles(int uid);
	/**
	 * 鏍规嵁瑙掕壊id鑾峰彇鍙互璁块棶鐨勬墍鏈夎祫婧�
	 * @param roleId
	 * @return
	 */
	public List<Resource> listRoleResource(int roleId);
	
	public void addRoleResource(int roleId,int resId);
	
	public void deleteRoleResource(int roleId,int resId);
	
	public RoleResource loadResourceRole(int roleId,int resId);
}
