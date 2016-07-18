package com.cmcc.shiro.test;

import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import ch.qos.logback.core.subst.Token;

public class ShiroTest {
    
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
    
    @Test
    public void testBase() {
        try {
            Subject subject = getLoginSubject("kh", "123");
            System.out.println(subject.getPrincipal());
        } catch (UnknownAccountException e) {
            System.out.println("用户名不存在！");
        } catch (IncorrectCredentialsException e) {
            System.out.println("用户密码不正确！");
        }
    }
    
    @Test
    public void testStatic() {
        try {
            Subject subject = getLoginSubject("kh", "1234");
            System.out.println(subject.getPrincipal());
        } catch (UnknownAccountException e) {
            System.out.println("用户名不存在！");
        } catch (IncorrectCredentialsException e) {
            System.out.println("用户密码不正确！");
        }
    }
    
    //测试登陆者是否有某个角色
    @Test
    public void testRole() {
        Subject subject = getLoginSubject("kh", "123");
        //通过subject判断"kh"是否有r1角色
        System.out.println(subject.hasRole("r1"));      //true
        System.out.println(subject.hasRole("r3"));      //false
        System.out.println(subject.hasAllRoles(Arrays.asList("r1", "r2")));      //true
        System.out.println(subject.hasRoles(Arrays.asList("r1", "r2", "r3")));      //返回的是一个数组，存储的是对每个角色的判断结果
    }
    
    //测试登陆者是否有某个权限
    @Test
    public void testPermission() {
        Subject subject = getLoginSubject("kh", "123");
        System.out.println(subject.isPermitted("user:create"));     //true
        System.out.println(subject.isPermitted("dept:create"));     //true。说明权限dept就相当于dept:*
        System.out.println(subject.isPermitted("dept:create:person"));  //true
        System.out.println(subject.isPermitted("user:create:1"));   //true
        System.out.println(subject.isPermitted("topic:view"));  //true
        System.out.println(subject.isPermitted("topic:*"));  //false
    }
    
    //测试自定义的permission解析器
    @Test
    public void testMyPermission() {
        Subject subject = getLoginSubject("kh", "123");
        System.out.println(subject.getPrincipal());     //konghao@gmail.com
        System.out.println(subject.isPermitted("classroom:add"));      //true
        System.out.println(subject.isPermitted("+user"));      //true
        System.out.println(subject.isPermitted("+topic+create"));      //true
    }
}
