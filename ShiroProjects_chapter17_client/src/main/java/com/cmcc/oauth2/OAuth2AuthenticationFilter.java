package com.cmcc.oauth2;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.druid.util.StringUtils;

public class OAuth2AuthenticationFilter extends AuthenticatingFilter {      //AuthenticatingFilter与AuthenticationFilter的区别、AuthorizationFilter的区别

    //oauth2 authc code参数名
    private String authcCodeParam = "code";
    //客户端id
    private String clientId;
    //服务器登录成功/失败后重定向到的客户端地址
    private  String redirectUrl;
    //oauth2服务器响应类型
    private String responseType = "code";
    
    private String failureUrl;
    
    public void setAuthcCodeParam(String authcCodeParam) {
        this.authcCodeParam = authcCodeParam;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest)request;
        String code = req.getParameter(authcCodeParam);
        return new OAuth2Token(code);
    }
    
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }
    
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        String error = request.getParameter("error");
        String errorDescription = request.getParameter("error_description");
        if(!StringUtils.isEmpty(error)) {
            //如果服务器返回了错误
            WebUtils.issueRedirect(request, response, failureUrl + "?error=" + error + "error_description" + errorDescription);
            return false;
        }
        
        Subject subject = getSubject(request, response);
        
        String authcCodeValue = request.getParameter(authcCodeParam);
        System.out.println("+++++++++++++++++++++++++++++++++++ authcCode = " + authcCodeValue);
        boolean isAuthenticated = subject.isAuthenticated();
        System.out.println("+++++++++++++++++++++++++++++++++++ subject.isAuthenticated() = " + isAuthenticated);
        
        if(!subject.isAuthenticated()) {
            if(StringUtils.isEmpty(request.getParameter(authcCodeParam))) {
                //如果用户没有身份验证，且没有auth code，则重定向到服务端授权
                saveRequestAndRedirectToLogin(request, response);
                return false;
            }
        }
        
        return executeLogin(request, response);
    }
    
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        issueSuccessRedirect(request, response);
        return false;
    }
    
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        Subject subject2 = getSubject(request, response);
        if(subject2.isAuthenticated() || subject2.isRemembered()) {
            try {
                issueSuccessRedirect(request, response);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } else {
            try {
                WebUtils.issueRedirect(request, response, failureUrl);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        
        return false;
    }
    
}
