package com.cmcc.utils;

import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

/*
 * 获取数据库连接池
 */
public class JdbcTemplateUtils {

    private static JdbcTemplate jdbcTemplate;
    
    public static JdbcTemplate jdbcTemplate() {
        if(jdbcTemplate == null) {
            return createJdbcTemplate();
        }
        
        return jdbcTemplate;
    }
    
    private static JdbcTemplate createJdbcTemplate() {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/shiro");
        ds.setUsername("root");
        ds.setPassword("root");
        
        return new JdbcTemplate(ds);
    }
}
