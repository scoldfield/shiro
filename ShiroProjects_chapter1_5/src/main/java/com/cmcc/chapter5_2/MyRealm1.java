package com.cmcc.chapter5_2;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/*
 * 通过DefaultPasswordService配合PasswordMatcher实现简单的加密与验证服务
 */
public class MyRealm1 extends AuthorizingRealm{

    //passwordService通过ini配置文件注入对象，具体使用DefaultPasswordService
    private PasswordService passwordService;
    public void setPasswordService(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    /*
     * 返回AuthenticationInfo的作用：
       myRealm 间接继承了 AuthenticatingRealm，myRealm会将获 取 到 AuthenticationInfo信息传递给父类，
                 父类会使用credentialsMatcher来验证凭据是否匹配，如果不匹配将抛出 IncorrectCredentialsException异常
    */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        return new SimpleAuthenticationInfo("wu", passwordService.encryptPassword("123"), getName());
    }
    
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        // TODO Auto-generated method stub
        return null;
    }

}
