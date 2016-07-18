package com.cmcc.shiro.permission;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

//判断permission字符串到底由哪个permission处理器来解析
public class MyPermissionResolver implements PermissionResolver {

    public Permission resolvePermission(String permissionString) {
        if(permissionString.startsWith("+")) {
            //使用自定义处理器来解析permission字符串
            return new MyPermission(permissionString);
        }
        
        //使用默认的WildcardPermission来解析permission字符串
        return new WildcardPermission(permissionString);
    }

}
