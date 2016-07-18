package com.cmcc.shiro.realm;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;

public class MapRealm implements Realm{

    private static Map<String, String> users;
    
    //用来存储已经保存的账号、密码。这部分应该是从数据库中获取的
    static {
        users = new HashMap<String, String>();
        users.put("kh", "123");
        users.put("zs", "123");
    }

    public String getName() {
        return "mapRealm";
    }

    //判断这个Realm支持哪些token认证。这里判断MapRealm是否支持UsernamePasswordToken认证
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    //获取传入的token， 进行认证
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String username = token.getPrincipal().toString();      //获取用户名
        String password = new String((char[])token.getCredentials());    //获取密码
//        System.out.println(username+", "+password);
        
        if(!users.containsKey(username)) {
            throw new UnknownAccountException("用户名不存在！");
        }
        if(!password.equals(users.get(username))) {
            throw new IncorrectCredentialsException("用户密码不正确！");
        }
//        AuthenticationInfo info = new SimpleAuthenticationInfo(username, password, getName());
//        return info;
        return null;
    }
}
