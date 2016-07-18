package com.cmcc.web.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/*
 * 会话监听器：用于监听会话的创建、过期、停止事件
 */
public class MySessionListener1 implements SessionListener {

    //会话创建时触发
    @Override
    public void onStart(Session session) {
        System.out.println("会话创建：" + session.getId());
    }

    //会话退出/过期时触发
    @Override
    public void onStop(Session session) {
        System.out.println("会话停止：" + session.getId());
    }
    
    //会话过期时触发
    @Override
    public void onExpiration(Session session) {
        System.out.println("会话过期：" + session.getId());
    }

}
