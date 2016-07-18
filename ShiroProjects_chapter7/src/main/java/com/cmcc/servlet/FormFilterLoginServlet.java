package com.cmcc.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;

/*
 * 使用表单拦截器进行身份认证
 * 1、shiro自带的FormAuthenticationFilter类已经实现了所有的认证过程
 * 2、我们只需要自定义一个FormFilterLoginServlet来处理认证失败时候报错的错误即可
 */
@WebServlet(name="formFilterLoginServlet", urlPatterns = "/formfilterlogin")
public class FormFilterLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
    
    /*
                测试时
                输入 http://localhost:8080/chapter7/role，会跳转到“/formfilterlogin”登录表单，提交表单如
                果 authc 拦截器登录成功后，会直接重定向会之前的地址“/role”；假设我们直接访问
            “/formfilterlogin”的话登录成功将直接到默认的 successUrl
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String errorClassName = (String) req.getAttribute("shiroLoginFailure"); //注意：这里不是getParameter(), 而是getAttribute()
        if(UnknownAccountException.class.getName().equals(errorClassName)) {
            req.setAttribute("error", "用户名/密码错误");
        } else if (IncorrectCredentialsException.class.getName().equals(errorClassName)) {
            req.setAttribute("error", "用户名/密码错误");
        } else if (errorClassName != null) {
            req.setAttribute("error", "未知错误：" + errorClassName);
        }
        
        req.getRequestDispatcher("/WEB-INF/jsp/formfilterlogin.jsp").forward(req, resp);
    }
}
