package com.cmcc.shiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/*
 * shiro中提供的加密方法
 */
public class TestEncode {
    
    //shiro提供的md5加密
    @Test
    public void md5Encode() {
        //将字符串"123"进行md5加密
        String code = new Md5Hash("123").toString();
        System.out.println(code);
    }
    
    //shiro提供的带有密码盐salt的md5加密
    @Test
    public void md5EncodeBySalt() {
        //将字符串"123"进行加盐salt的md5加密,其中"user"即为salt
        String code = new Md5Hash("123", "user").toString();
        System.out.println(code);
    }
    

    //通过subject获取登录用户的username和password
    private Subject getLoginSubject(String username, String password) {
        SecurityManager manager = new IniSecurityManagerFactory("classpath:shiro.ini").createInstance();
        SecurityUtils.setSecurityManager(manager);
        Subject subject = SecurityUtils.getSubject();
        
        //手动新建一个token用于测试，实际中是通过页面获取的
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);
        return subject;
    }
    
    //PasswordService中自带的加密方法。默认用的是SHA-256加密算法
    @Test
    public void testPasswordService() {
        DefaultPasswordService service = new DefaultPasswordService();
        String password = service.encryptPassword("123");
        System.out.println(password);
    }
    
    
    @Test
    public void testPasswordRealm() {
        //无论如何，登陆人认证的时候密码都会认证，即使用户名不一样，密码照样与SimpleAuthenticationInfo(Object principal, Object credentials, String realmName)中的credentials进行认证。所以一旦密码不正确就肯定会报错
        getLoginSubject("admin", "111");
        
        
    }

}
