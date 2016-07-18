package com.cmcc.chapter5_3;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/*
    密码重试次数限制:
            如在 1 个小时内密码最多重试 5 次，如果尝试次数超过 5 次就锁定 1 小时，1 小时后可再
            次重试，如果还是重试失败，可以锁定如 1 天，以此类推，防止密码被暴力破解。我们通
            过继承 HashedCredentialsMatcher，且使用 Ehcache 记录重试次数和超时时间。
 */

public class RetryLimitHashedCredentialsMatcher
        extends HashedCredentialsMatcher {
    
    private Ehcache passwordRetryCache;
    public void setPasswordRetryCache(Ehcache passwordRetryCache) {
        this.passwordRetryCache = passwordRetryCache;
    }

    public boolean doCredenticalsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        
        //retry count+1
        Element element = passwordRetryCache.get(username);
        if(element == null) {
            element = new Element(username, new AtomicInteger(0));
            passwordRetryCache.put(element);
        }
        
        AtomicInteger retryCount = (AtomicInteger) element.getObjectValue();
        if(retryCount.incrementAndGet() > 5) {      //AtomicInteger.incrementAndGet()表示以原子方式将当前值加 1。
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }
        
        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }
        return matches;
    }
}
