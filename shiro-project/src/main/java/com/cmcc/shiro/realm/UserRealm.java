package com.cmcc.shiro.realm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.cmcc.shiro.model.Resource;
import com.cmcc.shiro.model.User;
import com.cmcc.shiro.service.IUserService;
import com.cmcc.shiro.web.InitServlet;

public class UserRealm extends AuthorizingRealm {

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		User user = ((User)principals.getPrimaryPrincipal());
		int uid = user.getId();
		System.out.println(user.getId()+","+user.getNickname());
		IUserService userService = (IUserService)InitServlet.getBean("userService");
		List<String> roles = userService.listRoleSnByUser(uid);
		List<Resource> reses = userService.listAllResource(uid);
		List<String> permissions = new ArrayList<String>();
		for(Resource r:reses) {
			permissions.add(r.getUrl());
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setRoles(new HashSet<String>(roles));
		info.setStringPermissions(new HashSet<String>(permissions));
		return info;
	}
	

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		IUserService userService = (IUserService)InitServlet.getBean("userService");
		String username = token.getPrincipal().toString();
		String password = new String((char[])token.getCredentials());
		User user = userService.login(username, password);
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
		info.setCredentialsSalt(ByteSource.Util.bytes(user.getUsername()));
		return info;
	}

}
