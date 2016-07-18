package com.cmcc.service;

import com.cmcc.entity.Role;

public interface RoleService {

    public Role createRole(Role role);
    public void deleteRole(Long roleId);
    //添加角色-权限之间关系
    public void correlationPermissons(Long roleId, Long...permissionIds);
    //移除角色-权限之间关系
    public void uncorrelationPermissions(Long roleId, Long... permissionIds);
}
