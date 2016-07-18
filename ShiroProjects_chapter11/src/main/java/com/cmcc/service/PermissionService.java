package com.cmcc.service;

import com.cmcc.entity.Permission;

public interface PermissionService {

    public Permission createPermission(Permission permission);
    public void deletePermission(Long permissionId);
}
