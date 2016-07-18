package com.cmcc.credentials;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/*
 * 0、需要将该自定义类配置到shiro.ini中，写在[main]下面，如 credentialsMatcher=com.cmcc.credentials.RetryLimitHashedCredentialsMatcher。当然还需要为RetryLimitHashedCredentialsMatcher的父类的其他属性赋值，无论是通过IOC方式还是在子类中赋值都可以
 * 1、该类用来实例化UserRealm的父类的父类AuthenticatingRealm的credentialsMatcher参数，通过IOC注入
 * 2、credentialsMatcher对象的作用就是用来对UserRealm子类中的doGetAuthenticationInfo()方法返回的AuthenticationInfo对象进行认证，
 *    该对象中封装了principal + hashedCredentials + credentialsSalt + realmName
 * 3、实际的认证操作在HashedCredentialsMatcher类中实现，自定义类RetryLimitHashedCredentialsMatcher只是在认证的时候增加了密码输入次数的判断，当然也可以再增加锁定、定时解锁等操作
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher{

    private Ehcache passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher() {
        CacheManager cacheManager = CacheManager.newInstance(CacheManager.class.getClassLoader().getResource("ehcache.xml"));
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");   //名为"passwordRetryCache"cache已经在配置文件ehcache.xml中配置
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String)token.getPrincipal();
        //retry count + 1
        Element element = passwordRetryCache.get(username);
        if(element == null) {
            element = new Element(username , new AtomicInteger(0));
            passwordRetryCache.put(element);
        }
        AtomicInteger retryCount = (AtomicInteger)element.getObjectValue();
        if(retryCount.incrementAndGet() > 5) {      //retryCount.incrementAndGet()表示自增再获取该变量，相当于++retryCount
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }

        //实际的"认证"操作是在父类HashedCredentialsMatcher中实现的
        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }
        return matches;
    }
}
