package com.cmcc.jcaptcha;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.servlet.OncePerRequestFilter;

public class JCaptchaFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(ServletRequest request,
            ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        
        resp.setDateHeader("Expires", 0L);
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.setHeader("Cache-Control", "post-check=0, pre-check=0");
        resp.setHeader("Pragma", "np-cache");
        resp.setContentType("image/jpeg");
        
        String id = req.getRequestedSessionId();
        BufferedImage bi = JCaptcha.captchaService.getImageChallengeForID(id);
        
        ServletOutputStream out = resp.getOutputStream();
        
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } catch (Exception e) {
            out.close();
        }
        
    }

}
