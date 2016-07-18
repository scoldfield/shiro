package com.cmcc.shiro.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		//�û���¼����֤��ֱ��ʹ��shiro��ʵ�֣�����Ҫ�����ֶ�ȥ�����ݿ���ʵ��
		Subject subject= SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		String msg = null;
		try {
			subject.login(token);
		} catch (UnknownAccountException e) {
			msg = "�û�������";
		} catch (IncorrectCredentialsException e) {
			msg = "�û��������";
		} catch (AuthenticationException e) {
			msg = "�����쳣��" + e.getMessage();
		}
		
		if(msg != null){
			request.setAttribute("msg", msg);
			request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
		} else {
			//��¼��֤�ɹ���ֱ����ת����Ŀ¼��Ҳ����ʾindex.jsp
			response.sendRedirect(request.getContextPath()+"/");
		}
	}

}
