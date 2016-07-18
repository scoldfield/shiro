package com.cmcc.web.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.as.response.OAuthASResponse.OAuthAuthorizationResponseBuilder;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.druid.util.StringUtils;
import com.cmcc.constants.Constants;
import com.cmcc.service.ClientService;
import com.cmcc.service.OAuthService;

@Controller
public class AuthorizeController {

    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private ClientService clientService;
    
    @RequestMapping("/authorize")
    public Object authorize(Model model, HttpServletRequest request) throws OAuthSystemException, URISyntaxException {
        try {
            //构建OAuth授权请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            
            //检查传入的客户端id是否正确
            if(!oAuthService.checkClientId(oauthRequest.getClientId())) {
                OAuthResponse response =
                        OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                                .setErrorDescription(Constants.INVALID_CLIENT_DESCRIPTION)
                                .buildJSONMessage();
                return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }
            
            Subject subject = SecurityUtils.getSubject();
            //如果用户没有登陆，跳转到登陆页面
            if(!subject.isAuthenticated()) {
                if(!login(subject, request)) {      //登陆失败时跳转到登陆页面
                    model.addAttribute("client", clientService.findByClientId(oauthRequest.getClientId()));
                    return "oauth2login";
                }
            }
            
            String username = (String) subject.getPrincipal();
            //生成授权码
            String authorizationCode = null;
            //responseType目前仅支持CODE，另外还有TOKEN。即从request中获取的参数为CODE或TOKEN，即request.getParameter("response_type")为"code"或"token"，从而使用相应的OAuthValidater为CodeValidator或TokenValidator
            String responseType = oauthRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
            if(responseType.equals(ResponseType.CODE.toString())) {
                OAuthIssuerImpl authIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                authorizationCode = authIssuerImpl.authorizationCode(); //通过MD5算法生成OAuth Token或OAuth code
                oAuthService.addAuthCode(authorizationCode, username);
            }
            
            //进行OAuth响应构建。☆☆☆☆☆规律：构建OAuth响应好像都是通过OAuthASResponse类来完成的，无论是正常响应还是错误响应☆☆☆☆☆
            OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);
            //设置授权码
            builder.setCode(authorizationCode);
            //得到客户端重定向地址
            String redirectURI = oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI);
            
            //构建响应
            final OAuthResponse response = builder.location(redirectURI).buildQueryMessage();
            
            //根据OAuthResponse返回ResponseEntity响应
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(response.getResponseStatus()));
            
        } catch (OAuthProblemException e) {
            //出错处理
            String redirectUri = e.getRedirectUri();
            if(OAuthUtils.isEmpty(redirectUri)) {
                //告诉客户端没有传入redirectUri直接报错
                return new ResponseEntity("OAuth callback url needs to be provided by client!!!", HttpStatus.NOT_FOUND);
            }
            
            //返回错误信息(如？error)
            final OAuthResponse response = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_FOUND)
                    .error(e).location(redirectUri).buildQueryMessage();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(response.getLocationUri()));
            return new ResponseEntity<>(headers, HttpStatus.valueOf(response.getResponseStatus()));
        }
    }
    
    private boolean login(Subject subject, HttpServletRequest request) {
        if("get".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return false;
        }
        
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        
        try {
            subject.login(token);
            return true;
        } catch (Exception e) {
            request.setAttribute("error", "登陆失败:" + e.getClass().getName());
            return false;
        }
    }
}
