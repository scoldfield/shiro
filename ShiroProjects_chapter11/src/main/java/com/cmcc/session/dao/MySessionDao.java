package com.cmcc.session.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cmcc.utils.JdbcTemplateUtils;
import com.cmcc.utils.SerializableUtils;

public class MySessionDao extends CachingSessionDAO{
    
    private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();
    
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        String sql = "insert into sessions(id, session) values(?,?)";
        jdbcTemplate.update(sql, sessionId, SerializableUtils.serialize(session));
        return session.getId();
    }

    @Override
    protected void doUpdate(Session session) {
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
            return; //session已经过期/停止，没必要再更新了
        }
        
        String sql = "update sessions set session=? where id=?";
        jdbcTemplate.update(sql, SerializableUtils.serialize(session), session.getId());
    }

    @Override
    protected void doDelete(Session session) {
        String sql = "delete from sessions where id=?";
        jdbcTemplate.update(sql, session.getId());
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String sql = "select session from sessions where id=?";
        List<String> sessionList = jdbcTemplate.queryForList(sql, String.class, sessionId);
        if(sessionList.size() == 0) {
            return null;
        }
        
        return SerializableUtils.deserialize(sessionList.get(0));
    }
}
