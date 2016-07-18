package com.cmcc.servlet;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;


public class AnyRolesFilter extends AccessControlFilter {

    private String unauthorizedUrl = "/unauthorized.jsp";
    private String loginUrl = "/login.jsp";
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request,
            ServletResponse response, Object mappedValue) throws Exception {
        
        String[] roles = (String[]) mappedValue;
        if(roles == null) {
            return true;    //如果没有设置角色参数，默认成功
        }
        
        for(String role : roles) {
            if(getSubject(request, response).hasRole(role)) {
                //只要登陆用户有配置文件ini中的任意角色就通过该Filter，继续执行链中的其他Filter
                return true;
            }
        }
        
        return false;   //跳到onAccessDenied处理
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request,
            ServletResponse response) throws Exception {
        
        Subject subject = getSubject(request, response);
        if(subject.getPrincipal() == null) {
            //表示没有登陆，重定向到登陆界面
            saveRequest(request);
            WebUtils.issueRedirect(request, response, loginUrl);
        } else {
            if(StringUtils.hasText(unauthorizedUrl)) {
                //如果存在未授权页面unauthorized.jsp，那么跳转过去
                WebUtils.issueRedirect(request, response, unauthorizedUrl);
            } else {
                //否则返回401未授权状态码
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        
        return false;
    }

}
