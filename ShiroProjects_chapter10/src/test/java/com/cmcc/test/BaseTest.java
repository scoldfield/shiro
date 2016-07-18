package com.cmcc.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.After;
import org.junit.Before;

public class BaseTest {

    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
        
    }
    
    protected void login(String configFile, String username, String password) {
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory(configFile);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//        token.setHost("10.83.1.1");
        subject.login(token);
    }
    
    public Subject subject() {
        return SecurityUtils.getSubject();
    }
}
