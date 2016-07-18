package com.cmcc.servlet;

import java.io.IOException;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

//@WebServlet(name = "loginServlet", urlPatterns = "/login")    //如果在web.xml中配置了/login的url是LoginServlet来处理，那么就不需要用这个声明了
public class LoginServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String error = null;
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    
	    Subject subject = SecurityUtils.getSubject();
	    UsernamePasswordToken token = new UsernamePasswordToken(username, password);
	    
	    try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            error = "用户名/密码错误";
        } catch (IncorrectCredentialsException e) {
            error = "用户名/密码错误";
        }
	    
	    if(error != null) {    //出错了，返回登陆界面
	        request.setAttribute("error", error);
	        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
	    } else {   //登陆成功
	        request.getRequestDispatcher("/WEB-INF/jsp/loginSuccess.jsp").forward(request, response);
	    }
	}

}
