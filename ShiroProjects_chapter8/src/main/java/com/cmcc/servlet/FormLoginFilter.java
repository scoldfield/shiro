package com.cmcc.servlet;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;

/*
 * shiro内置了基于表单登陆的拦截器，这里我们自己实现一个基于表单登陆的拦截器
 */
public class FormLoginFilter extends PathMatchingFilter {
    
    private String loginUrl = "/login.jsp";
    private String successUrl = "/";
    
    /*
     * onPreHandle：如果 url模式与请求 url匹配，那么会执行 onPreHandle，并把该拦截器配置的参数传入。默认什么不处理直接返回 true。
     * mappedValue：参数mappedValue是配置文件[urls]下的匹配的url对应的等号右边的值，类型是String[]
     */
    @Override
    protected boolean onPreHandle(ServletRequest request,
            ServletResponse response, Object mappedValue) throws Exception {
        
        if(SecurityUtils.getSubject().isAuthenticated()) {
            //已经登陆过。返回true表示继续执行拦截器链FilterChain上的拦截器
            return true;
        }
        
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        if(isLoginRequest(req)) {
            //是访问/login.jsp页面
            if("post".equals(req.getMethod())) {    //"post"表单提交方式
                boolean loginSuccess = login(req);  //登陆
                if(loginSuccess) {
                    //登陆成功
                    return  redirectToSuccessUrl(req, resp); 
                }
            }
            
            return true;    //继续过滤器链
        } else {
            //保存当前地址并重新定向到登陆界面。没有登陆，且访问的是需要认证后才能访问的页面，那么就先记住想要访问的页面url，然后跳转到login.jsp页面
            saveRequestAndRedirectToLogin(req, resp);
            return false;   //返回 true 表示自己不处理且继续拦截器链执行，返回 false 表示自己已经处理了
        }
        
    }
    
    private boolean isLoginRequest(HttpServletRequest req) {
        return pathsMatch(loginUrl, WebUtils.getPathWithinApplication(req));
    }
    
    private boolean login(HttpServletRequest req) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
        } catch (Exception e) {
            req.setAttribute("shiroLoginFailure", e.getClass());
            return false;
        }
        return true;
    }
    
    private boolean redirectToSuccessUrl(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebUtils.redirectToSavedRequest(req, resp, successUrl);
        return false;
    }
    
    private void saveRequestAndRedirectToLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebUtils.saveRequest(req);
        WebUtils.issueRedirect(req, resp, loginUrl);
    }
}
