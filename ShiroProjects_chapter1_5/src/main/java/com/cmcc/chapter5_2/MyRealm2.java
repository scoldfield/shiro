package com.cmcc.chapter5_2;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/*
 * HashedCredentialsMatcher 实现密码验证服务
 * CredentialsMatcher的散列实现类 HashedCredentialsMatcher，和 之 前 的PasswordMatcher区别在于，
 * 它只用于密码验证，且可以提供自己的盐，而不是随机生成盐，且生成密码散列值的算法需要自己写，因为能提供自己的盐。
 */
public class MyRealm2 extends AuthorizingRealm {
    
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        String username = "liu";    //用户名即salt。从数据库中获取
        String password = "202cb962ac59075b964b07152d234b70";   //加密后的密码
        String salt2 = "202cb962ac59075b964b07152d234b70";
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password, getName());
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(username + salt2));   //盐是用户名+随机数
        return simpleAuthenticationInfo;
        
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        return null;
    }

}
