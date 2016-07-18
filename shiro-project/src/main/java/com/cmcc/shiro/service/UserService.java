package com.cmcc.shiro.service;

import java.util.List;

import javax.inject.Inject;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Service;

import com.cmcc.shiro.dao.IRoleDao;
import com.cmcc.shiro.dao.IUserDao;
import com.cmcc.shiro.kit.ShiroKit;
import com.cmcc.shiro.model.Resource;
import com.cmcc.shiro.model.Role;
import com.cmcc.shiro.model.User;

@Service("userService")
public class UserService implements IUserService {
	@Inject
	private IUserDao userDao;
	@Inject
	private IRoleDao roleDao;
	
	public void add(User user) {
		if(ShiroKit.isEmpty(user.getUsername())||ShiroKit.isEmpty(user.getPassword())) {
			throw new RuntimeException("鐢ㄦ埛鍚嶆垨鑰呭瘑鐮佷笉鑳戒负绌猴紒");
		}
		user.setPassword(ShiroKit.md5(user.getPassword(), user.getUsername()));
		userDao.add(user);
	}

	public void delete(int id) {
		userDao.delete(id);
	}

	public void update(User user,List<Integer> rids) {
		roleDao.deleteUserRoles(user.getId());
		for(int rid:rids) {
			roleDao.addUserRole(user.getId(), rid);
		}
		userDao.update(user);
	}
	
	public void update(User user) {
		userDao.update(user);
	}

	public User load(int id) {
		return userDao.load(id);
	}

	public User loadByUsername(String username) {
		return userDao.loadByUsername(username);
	}

	public User login(String username, String password) {
		User u = userDao.loadByUsername(username);
		if(u==null) throw new UnknownAccountException("鐢ㄦ埛鍚嶆垨鑰呭瘑鐮佸嚭閿�");
		if(!u.getPassword().equals(ShiroKit.md5(password, username)))
			throw new IncorrectCredentialsException("鐢ㄦ埛鍚嶆垨鑰呭瘑鐮佸嚭閿�");
		if(u.getStatus()==0) throw new LockedAccountException("鐢ㄦ埛宸茬粡琚攣瀹�");
		return u;
	}

	public List<User> list() {
		return userDao.listUser();
	}

	public List<User> listByRole(int id) {
		return userDao.listByRole(id);
	}

	public List<Resource> listAllResource(int uid) {
		return userDao.listAllResource(uid);
	}

	public void add(User user, List<Integer> rids) {
		this.add(user);
		for(int rid:rids) {
			roleDao.addUserRole(user.getId(), rid);
		}
	}

	public List<String> listRoleSnByUser(int uid) {
		return userDao.listRoleSnByUser(uid);
	}

	public List<Role> listUserRole(int uid) {
		return userDao.listUserRole(uid);
	}

}
