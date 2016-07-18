package com.cmcc.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.junit.Test;

import com.cmcc.base.BaseTest;

public class UserRealmTest extends BaseTest{

    @Override
    public void tearDown() throws Exception {
        userService.changePassword(u1.getId(), password);
        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm) securityManager.getRealms().iterator().next();
        userRealm.clearCachedAuthenticationInfo(subject().getPrincipals());
        
        super.tearDown();
    }
    
    @Test
    public void testClearCachedAuthenticationInfo(){
        login(u1.getUsername(), password);
        userService.changePassword(u1.getId(), password + "1");
        
        //1、先删除该principal
        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm) securityManager.getRealms().iterator().next();
        userRealm.clearCachedAuthenticationInfo(subject().getPrincipals());

        //2、再重新login
        login(u1.getUsername(), password + "1");
    }
    
    @Test
    public void testClearCachedAuthorizationInfo() {
        login(u1.getUsername(), password);
        subject().checkRole(r1.getRole());  //check方法将user和role绑定到AuthorizationInfo中了
        userService.correlationRoles(u1.getId(), r2.getId());   //重新关联角色

        //1、先删除该principal
        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm) securityManager.getRealms().iterator().next();
        userRealm.clearCachedAuthorizationInfo(subject().getPrincipals());
        
        //2、再重新check
        subject().checkRole(r2.getRole());  //重新check将user和role绑定到AuthorizationInfo中
    }
    
    @Test
    public void testClearCache() {
        login(u1.getUsername(), password);
        subject().checkRole(r1.getRole());
        
        userService.changePassword(u1.getId(), password + "1");
        userService.correlationRoles(u1.getId(), r2.getId());
        
        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm) securityManager.getRealms().iterator().next();
        userRealm.clearCache(subject().getPrincipals());
        
        login(u1.getUsername(), password + "1");
        subject().checkRole(r2.getRole());
    }
}
