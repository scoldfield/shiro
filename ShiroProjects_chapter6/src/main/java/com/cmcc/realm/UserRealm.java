package com.cmcc.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.cmcc.entity.User;
import com.cmcc.service.UserService;
import com.cmcc.service.UserServiceImpl;


public class UserRealm extends AuthorizingRealm {
    
    private UserService userService = new UserServiceImpl();
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = userService.findByUsername(username);
        if(user == null) {
            throw new UnknownAccountException();    //没找到账号
        }
        
        if(Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException();     //账号锁定
        }
        
        /*
         * doGetAuthenticationInfo 获取身份验证相关信息：
         * 1、交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以再次判断或自定义实现；
         * 2、另外如果密码重试此处太多将抛出超出重试次数异常 ExcessiveAttemptsException；
         * 3、组装 SimpleAuthenticationInfo 信息时，需要传入：身份信息（用户名）、凭据（密文密码）、盐（username+salt）， CredentialsMatcher使用盐加密传入的明文密码和此处的密文密码进行匹配
         */
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), 
                user.getPassword(), 
                ByteSource.Util.bytes(user.getCredentialsSalt()),   //该参数规定必须为ByteSource类型，因此使用ByteSource.Util.bytes()方法来将字符串转换为ByteSource类型。本文使用的salt = username + salt
                getName());
        
        return authenticationInfo;
    }

    /*
     * 
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        //PrincipalCollection 是一个身份集合，因为我们现在就一个 Realm，所以直接调用 getPrimaryPrincipal 得到之前传入的用户名即可
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.findRoles(username));
        authorizationInfo.setStringPermissions(userService.findPermissions(username));
        return authorizationInfo;
    }

}
