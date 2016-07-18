package com.cmcc.web.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;

/*
 * 会话监听器：用于监听会话的创建、过期、停止中的任一事件
 * SessionListenerAdapter类已经实现了SessionListener接口，因此我们不需要自己去实现SessionListener的所有接口，只需要按照我们的需要实现相应的接口即可
 */
public class MySessionListener2 extends SessionListenerAdapter {

    //比如我们想监听session的创建，那么只需要重写onStart()方法即可
    @Override
    public void onStart(Session session) {
        System.out.println("会话创建" + session.getId());
    }
}
