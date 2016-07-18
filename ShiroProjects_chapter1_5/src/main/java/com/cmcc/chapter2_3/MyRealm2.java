package com.cmcc.chapter2_3;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;

public class MyRealm2 implements Realm {

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String username = (String) token.getPrincipal();
        String password = new String((char[])token.getCredentials());
        
        if(!"zhang".equals(username)) {
            throw new UnknownAccountException();    //用户名错误
        }
        if(!"123".equals(password)) {
            throw new IncorrectCredentialsException();  //密码错误
        }
        
        return new SimpleAuthenticationInfo(username, password, getName());
    }

}
