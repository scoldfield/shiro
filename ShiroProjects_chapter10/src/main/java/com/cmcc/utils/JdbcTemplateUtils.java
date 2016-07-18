package com.cmcc.utils;

import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

/*
 * 创建数据库链接
 */
public class JdbcTemplateUtils {

    private static JdbcTemplate jdbcTemplate;
    
    public static JdbcTemplate jdbcTemplate() {
        //需要判断一下，因为有可能之前已经建立链接了，且链接还没有断开，那么就不需要在重新创建新的链接
        if(jdbcTemplate == null) {
            jdbcTemplate = createJdbcTemplate();
        }
        
        return jdbcTemplate;
    }
    
    
    public static JdbcTemplate createJdbcTemplate() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/shiro");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        
        return new JdbcTemplate(dataSource);
    }
}
