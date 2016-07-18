package com.cmcc.chapter2_4;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class LoginLogoutTest4 {
    
    @Test
    public void test() {
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro-jdbc-realm.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        subject.login(token);
        
        if(subject.isAuthenticated()) {
            System.out.println("认证成功！");
        } else {
            System.out.println("认证失败！");
        }
        
        subject.logout();
    }

}
