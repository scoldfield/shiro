package com.cmcc.chapter2_5;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import junit.framework.Assert;

public class LoginLogoutTest5 {
    
    public void login(String configFile) {
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:" + configFile);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        subject.login(token);
    }

    @Test
    public void testAllSuccessfulStrategyWithSuccess() {
        login("shiro-authenticator-all-success.ini");
        Subject subject = SecurityUtils.getSubject();
        
        //得到一个身份集合，其包含了Realm验证成功的身份信息
        PrincipalCollection principals = subject.getPrincipals();
        
        Iterator iterator = principals.iterator();
        while(iterator.hasNext()) {
            String username = (String) iterator.next();
            System.out.println(username + "登陆成功");
        }
    }
    
    @Test(expected = UnknownAccountException.class)
    public void testAllSuccessfulStrategyWithFail() {
        login("shiro-authenticator-all-fail.ini");
        Subject subject = SecurityUtils.getSubject();
    }
}
