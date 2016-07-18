package com.cmcc.shiro.permission;

import org.apache.shiro.authz.Permission;

public class MyPermission implements Permission {

    private String resourceId;
    private String operator;
    private String instanceId;
    
    public MyPermission() {
        
    }
    
    public MyPermission(String permissionStr) {
        String[] permissionArr = permissionStr.split("\\+");
        if(permissionArr.length >= 2) {
            //至少为"+user"模式。注意：按照"+"号切分后[0]为空，即加号前的空字符；[1]为user
            this.resourceId = permissionArr[1];
        }
        if(this.getResourceId() == null || "".equals(this.getResourceId())) {
            this.setResourceId("*");
        }
        if(permissionArr.length >= 3) {
            //至少为"+user+*"模式
            this.operator = permissionArr[2];
        }
        if(permissionArr.length >= 4) {
            //至少为"+user+delete+1"模式
            this.instanceId = permissionArr[3];
        }
        if(this.getInstanceId() == null || "".equals(this.getInstanceId())) {
            this.setInstanceId("*");
        }
        
//        if(permissionArr.length > 1) {
//            //至少为"+user"模式。注意：按照"+"号切分后[0]为空，即加号前的空字符；[1]为user
//            this.setResourceId(permissionArr[1]);
//        }
//        if(this.getResourceId() == null || "".equals(this.getResourceId())) {
//            this.setResourceId("*");
//        }
//        if(permissionArr.length > 2) {
//            //至少为"+user+*"模式
//            this.setOperator(permissionArr[2]);
//        }
//        if(permissionArr.length > 3) {
//            //至少为"+user+delete+1"模式
//            this.setInstanceId(permissionArr[3]);
//        }
//        if(this.getInstanceId() == null || "".equals(this.getInstanceId())) {
//            this.setInstanceId("*");
//        }
    }
    
    //实现permission的比较
    public boolean implies(Permission p) {
        if(!(p instanceof MyPermission)) {
            return false;
        }
        
        MyPermission mp = (MyPermission)p;      //p中存储的权限是从数据库中获取的正确的permission，this中存储的是客户端传递的permission
        if(!this.resourceId.equals("*") && !this.resourceId.equals(mp.getResourceId())) {
            return false;
        }
        if(!this.operator.equals("*") && !this.operator.equals(mp.getOperator())) {
            return false;
        }
        if(!this.instanceId.equals("*") && !this.instanceId.equals(mp.getInstanceId())) {
            return false;
        }
        return true;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String toString() {
        return "MyPermission [resourceId=" + resourceId + ", operator="
                + operator + ", instanceId=" + instanceId + "]";
    }
    
}
