package com.cmcc.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.cmcc.entity.User;
import com.cmcc.service.UserService;

/**
 * <p>User: Zhang Kaitao
 * <p>Date: 14-1-28
 * <p>Version: 1.0
 */
public class UserRealm extends AuthorizingRealm {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.findRoles(username));
        authorizationInfo.setStringPermissions(userService.findPermissions(username));

        return authorizationInfo;
    }

    /*
     * 三个方法：doGetAuthenticationInfo() <—— getAuthenticationInfo() <—— assertCredentialsMatch()
     * 
     * 从父类(AuthorizingRealm)的父类(AuthenticatingRealm)中可以看出，getAuthenticationInfo()方法会调用doGetAuthorizationInfo()方法去获取AuthenticationInfo对象，并调用
     * 该类中的assertCredentialsMatch()方法，用于用户名/密码认证。
     * 
     * 1、UserRealm继承父类就不仅继承了doGetAuthenticationInfo()、doGetAuthorizationInfo()两个方法，而是继承了所有方法，包括getAuthenticationInfo()和assertCredentialsMatch()方法
     * 2、子类中的getAuthenticationInfo()方法会调用assertCredentialsMatch()方法和doGetAuthenticationInfo()方法返回的AuthenticationInfo对象，因为UserRealm实例化的时候使用的是子类来实例化的
     * 3、父类 引用 = 实例化：如果实例化使用的父类，那么调用的就是父类方法，如果实例化使用的是子类，那么就是调用子类方法
     * 
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String username = (String)token.getPrincipal();

        User user = userService.findByUsername(username);

        if(user == null) {
            throw new UnknownAccountException();//没找到帐号
        }

        if(Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException(); //帐号锁定
        }

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        return authenticationInfo;
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
