package com.cmcc.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.cmcc.shiro.permission.MyPermission;

public class StaticRealm extends AuthorizingRealm{

    /*
     * 用来判断授权的
     * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //添加授权信息。然后通过解析器来解析permission。这一步应该是从数据库中获取的
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //手动为principals添加角色role和权限permission。而这两项实际应该是从数据库中获取的
        info.addRole("r1");
        info.addRole("r2");
        info.addStringPermission("+user+*");
        info.addObjectPermission(new MyPermission("+topic+create"));
        info.addObjectPermission(new MyPermission("+topic+delete+2"));
        return info;
    }

    /*
     * 用来判断认证的
     * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        
        String username = token.getPrincipal().toString();
        String password = new String((char[])token.getCredentials());
        
        if(!"kh".equals(username)) throw new UnknownAccountException("用户名不存在！");
        if(!"123".equals(password)) throw new IncorrectCredentialsException("用户密码错误！");
        return new SimpleAuthenticationInfo("konghao@gmail.com", password, getName());
    }

}
