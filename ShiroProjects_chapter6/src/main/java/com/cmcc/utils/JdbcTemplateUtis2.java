package com.cmcc.utils;

import org.springframework.jdbc.core.JdbcTemplate;

/*
 * 通过注入的方式获取JdbcTemplate
 */
public class JdbcTemplateUtis2 {

    private JdbcTemplate jdbcTemplate;
    
    public JdbcTemplate jdbcTemplate() {
        return jdbcTemplate;
    }
}
