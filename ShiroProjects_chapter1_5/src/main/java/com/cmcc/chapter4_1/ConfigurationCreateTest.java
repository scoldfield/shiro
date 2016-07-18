package com.cmcc.chapter4_1;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

/*
 * 使用ini配置文件实现shiro的认证和授权
 */
public class ConfigurationCreateTest {

    @Test
    public void configurationCreateTest() {
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro-config.ini");
        SecurityManager securityManager = factory.getInstance();
        
        //将SecurityManager设置到SecurityUtils方便全局使用
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        subject.login(token);
        
        Assert.assertTrue(subject.isAuthenticated());
    }
}
