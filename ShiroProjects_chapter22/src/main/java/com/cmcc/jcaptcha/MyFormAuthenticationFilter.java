package com.cmcc.jcaptcha;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter{

    @Override
    protected boolean onAccessDenied(ServletRequest request,
            ServletResponse response, Object mappedValue) throws Exception {
        if(request.getAttribute(getFailureKeyAttribute()) != null) {
            return true;    //如果验证码认证已经错了，那就直接跳过身份认证
        }
        return super.onAccessDenied(request, response, mappedValue);
    }
}
