package com.cmcc.chapter2_5;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.hamcrest.core.IsInstanceOf;

/*
 * 自定义Realm
 */
public class MyRealm1 implements Realm{

    //返回一个唯一的Realm名字
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    //判断此Realm是否支持此Token
    @Override
    public boolean supports(AuthenticationToken token) {
        //这里仅支持UsernamePasswordToken类型的Token
        return (token instanceof UsernamePasswordToken);
    }

    //根据Token获取认证信息
    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String username = (String) token.getPrincipal(); 
        String password = new String((char[]) token.getCredentials());
        
        if(!"zhang".equals(username)) {
            throw new UnknownAccountException();    //用户名错误
        }
        if(!"123".equals(password)) {
            throw new IncorrectCredentialsException();  //密码错误
        }
        
        return new SimpleAuthenticationInfo(username, password, getName());
    }

}
