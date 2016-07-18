package com.cmcc.shiro.permission;

import java.util.Arrays;
import java.util.Collection;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

public class MyRolePermissionResolver implements RolePermissionResolver{

    public Collection<Permission> resolvePermissionsInRole(String roleString) {
        //只要拥有角色roleString，就会完成"classroom:*"的权限认证
        if(roleString.contains("r1")) {
            return Arrays.asList((Permission)new WildcardPermission("classroom:*"));
        }
        return null;
    }

}
