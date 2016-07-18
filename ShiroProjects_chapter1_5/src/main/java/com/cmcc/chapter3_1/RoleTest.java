package com.cmcc.chapter3_1;

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

/*
 * Shiro 提供的 checkRole/checkRoles 和 hasRole/hasAllRoles 
 * 不同的地方是它在判断为假的情况下会抛出 UnauthorizedException 异常。
 */
public class RoleTest {
    
    public void login(String configFile) {
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:" + configFile);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        subject.login(token);
    }

    @Test
    public void testHasRole() {
        login("shiro-role.ini");
        Subject subject = SecurityUtils.getSubject();
        
        //判断拥有角色：role1
        boolean hasRole = subject.hasRole("role1");
        if(hasRole) {
            System.out.println("拥有role1");
        } else {
            System.out.println("不拥有role1");
            
        }
        
        //判断同时拥有角色：role1和role2
        boolean hasAllRole = subject.hasAllRoles(Arrays.asList("role1", "role2"));
        if(hasAllRole) {
            System.out.println("拥有role1和role2");
        } else {
            System.out.println("不同时拥有role1和role2");
        }
        
        //判断拥有角色：role1 and role2 and !role3
        boolean[] hasRoles = subject.hasRoles(Arrays.asList("role1", "role2", "role3"));
        if(hasRoles[0]) {
            System.out.println("拥有role1");
        }
        if(hasRoles[1]) {
            System.out.println("拥有role2");
        }
        if(!hasRoles[2]) {
            System.out.println("不拥有role3");
        }
    }
    
    //Shiro 提供的 checkRole/checkRoles 和 hasRole/hasAllRoles区别在于判断为假的情况下前者会抛出 UnauthorizedException 异常。
    @Test
    public void testCheckRole() {
        login("shiro-role.ini");
        Subject subject = SecurityUtils.getSubject();
        
        //断言拥有角色：role1
        subject.checkRole("role1");
        
        //断言拥有角色：role1 and role3。失败，抛出异常
        subject.checkRoles("role1", "role3");
    }
}
