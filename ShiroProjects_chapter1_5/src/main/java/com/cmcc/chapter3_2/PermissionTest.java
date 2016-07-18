package com.cmcc.chapter3_2;

import java.security.Principal;
import java.util.Arrays;
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

public class PermissionTest {
    
    public void login(String configFile) {
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:" + configFile);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        subject.login(token);
    }

    @Test
    public void testIsPermitted() {
        login("shiro-permission.ini");
        Subject subject = SecurityUtils.getSubject();
        
        //判断拥有权限：user:create
        boolean isPermitted = subject.isPermitted("user:create");
        if(isPermitted) {
            System.out.println("拥有权限：user:create");
        } else {
            System.out.println("不拥有权限：user:create");
            
        }
        
        //判断同时拥有权限：user:update and user:delete
        boolean idPermittedAll = subject.isPermittedAll("user:update", "user:delete");
        if(idPermittedAll) {
            System.out.println("同时拥有user:update and user:delete");
        } else {
            System.out.println("不同时拥有user:update and user:delete");
        }
        
    }
    
    //Shiro 提供的 checkPermission/checkPermissions 和 isPermitted/isPermittedAll区别在于判断为假的情况下前者会抛出 UnauthorizedException 异常。
    @Test
    public void testCheckRole() {
        login("shiro-role.ini");
        Subject subject = SecurityUtils.getSubject();
        
        //断言拥有权限：user:create
        subject.checkPermission("user:create");
        
        //断言拥有权限：user:delete and user:update。失败，抛出异常
        subject.checkPermissions("user:delete", "user:update");
    }
}
