package com.cmcc.web.shiro.filter;

import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

/*
 * 控制并发登陆的filter
 */
public class KickoutSessionControllerFilter extends AccessControlFilter{

    private String kickoutUrl;  //踢出后到的地址
    private boolean kickoutAfter = false;   //踢出之前登陆的/之后登陆的用户，默认踢出之前登陆的用户
    private int maxSession = 1; //同一个账号最大会话数，默认1
    
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }
    
    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }
    
    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }
    
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro-kickout-session");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request,
            ServletResponse response, Object mappedValue) throws Exception {
        return false;   //交给onAccessDenied()方法处理
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request,
            ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登陆，直接进行之后的流程
            return true;
        }
        
        String username = (String) subject.getPrincipal();
        Session session = subject.getSession();
        Serializable sessionId = session.getId();
        
        //TODO 同步控制
        //......
        
        
        //用户登陆后将登录名存储到缓存cache中
        Deque<Serializable> deque = cache.get(username);
        if(deque == null) {
            deque = new LinkedList<Serializable>();
            cache.put(username, deque);
        }
        
        //如果队列里没有此sessionId，且用户没有被踢出，那么放入队列
        if(!deque.contains(sessionId) && session.getAttribute("kickout") == null) {
            deque.push(sessionId);
        }
        
        //如果队列里的sessionId数超出最大会话数，开始踢人
        while(deque.size() > maxSession) {
            Serializable kickoutSessionId = null;
            if(kickoutAfter) {
                //如果踢出后者
                kickoutSessionId = deque.getLast();
            } else {
                kickoutSessionId = deque.getFirst();
            }
            
            try {
                Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if(kickoutSession != null) {
                    //设置会话的kickout属性表示踢出了
                    kickoutSession.setAttribute("kickout", true);
                }
            } catch (Exception e) {
            }
        }

        //如果被踢出了，直接退出，重定向到踢出后的地址
        if(session.getAttribute("kickout") != null) {
            //会话别踢出了
            try {
                subject.logout();   //移除与subject相关的session就是调用subject的logout()方法即可
            } catch (Exception e) {
            }
        }
        saveRequest(request);
        WebUtils.issueRedirect(request, response, kickoutUrl);
        
        return false;
    }
    
    
    
    
}
