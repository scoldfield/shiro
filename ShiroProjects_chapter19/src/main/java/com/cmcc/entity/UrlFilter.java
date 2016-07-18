package com.cmcc.entity;

public class UrlFilter {
    private Long id;
    private String name;     //url名称/描述
    private String url;     //地址
    private String roles;   //所需要的角色，可省略
    private String permissions; //所需要的权限，可省略
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getRoles() {
        return roles;
    }
    public void setRoles(String roles) {
        this.roles = roles;
    }
    public String getPermissions() {
        return permissions;
    }
    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
    
    
    @Override

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        UrlFilter urlFilter = (UrlFilter)obj;
        
        if(id != null ? !id.equals(urlFilter.id) : urlFilter.id != null) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "UrlFilter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", roles='" + roles + '\'' +
                ", permissions='" + permissions + '\'' +
                '}';
    }
}
