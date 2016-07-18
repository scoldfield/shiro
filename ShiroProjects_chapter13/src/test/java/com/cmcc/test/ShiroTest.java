package com.cmcc.test;


import javax.sql.DataSource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.cmcc.entity.Permission;
import com.cmcc.entity.Role;
import com.cmcc.entity.User;
import com.cmcc.realm.UserRealm;
import com.cmcc.service.PermissionService;
import com.cmcc.service.RoleService;
import com.cmcc.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-beans.xml", "classpath:spring-shiro.xml"})
@TransactionConfiguration(defaultRollback = false)
public class ShiroTest {

    @Autowired
    protected PermissionService permissionService;
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected UserRealm userRealm;
    
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    private void serDataSource(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }
    
    protected String password = "123";

    protected Permission p1;
    protected Permission p2;
    protected Permission p3;
    protected Role r1;
    protected Role r2;
    protected User u1;
    protected User u2;
    protected User u3;
    protected User u4;
    
    @Before
    public void setUp() {
        jdbcTemplate.update("delete from sys_users");
        jdbcTemplate.update("delete from sys_roles");
        jdbcTemplate.update("delete from sys_permissions");
        jdbcTemplate.update("delete from sys_users_roles");
        jdbcTemplate.update("delete from sys_roles_permissions");


        //1、新增权限
        p1 = new Permission("user:create", "用户模块新增", Boolean.TRUE);
        p2 = new Permission("user:update", "用户模块修改", Boolean.TRUE);
        p3 = new Permission("menu:create", "菜单模块新增", Boolean.TRUE);
        permissionService.createPermission(p1);
        permissionService.createPermission(p2);
        permissionService.createPermission(p3);
        //2、新增角色
        r1 = new Role("admin", "管理员", Boolean.TRUE);
        r2 = new Role("user", "用户管理员", Boolean.TRUE);
        roleService.createRole(r1);
        roleService.createRole(r2);
        //3、关联角色-权限
        roleService.correlationPermissions(r1.getId(), p1.getId());
        roleService.correlationPermissions(r1.getId(), p2.getId());
        roleService.correlationPermissions(r1.getId(), p3.getId());

        roleService.correlationPermissions(r2.getId(), p1.getId());
        roleService.correlationPermissions(r2.getId(), p2.getId());

        //4、新增用户
        u1 = new User("zhang", password);
        u2 = new User("li", password);
        u3 = new User("wu", password);
        u4 = new User("wang", password);
        u4.setLocked(Boolean.TRUE);
        userService.createUser(u1);
        userService.createUser(u2);
        userService.createUser(u3);
        userService.createUser(u4);
        //5、关联用户-角色
        userService.correlationRoles(u1.getId(), r1.getId());
    }
    
    @Test
    public void test() {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(u1.getUsername(), u1.getPassword());
        subject.login(token);
        
        Assert.assertTrue(subject.isAuthenticated());
        subject.checkRole("admin");
        subject.checkPermission("user:create");
        
        userService.changePassword(u1.getId(), password + "1");
        userRealm.clearCache(subject.getPrincipals());
        
        token = new UsernamePasswordToken(u1.getUsername(), u1.getPassword());
        subject.login(token);
        
    }
}