package com.cmcc.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import junit.framework.Assert;

public class SessionTest extends BaseTest {

    @Test
    public void testGetSession() throws Exception{
        login("classpath:shiro.ini", "zhang", "123");
        Subject subject = SecurityUtils.getSubject();   //默认情况下创建subject后就会创建相应的session
        
        //获取会话
        Session session = subject.getSession();
        
        System.out.println("获取会话ID" + session.getId());//获取会话ID
        System.out.println("获取当前登录用户主机地址" + session.getHost());//获取当前登录用户主机地址
        System.out.println("获取超时时间" + session.getTimeout());//获取超时时间。默认情况下是30分钟
        System.out.println("获取会话创建时间" + session.getStartTimestamp());//获取会话创建时间
        System.out.println("获取最后访问时间" + session.getLastAccessTime());//获取最后访问时间
        System.out.println("等待1秒钟...");
        Thread.sleep(1000);
        session.touch();//更新会话最后访问时间
        System.out.println("再次访问，获取最后访问时间" + session.getLastAccessTime());
        
        //会话属性操作
        session.setAttribute("key", "123");
        Assert.assertEquals("123", session.getAttribute("key"));
        session.removeAttribute("key");
    }
}
