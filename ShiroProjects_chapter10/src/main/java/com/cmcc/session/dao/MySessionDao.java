package com.cmcc.session.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cmcc.utils.JdbcTemplateUtils;
import com.cmcc.utils.SerializableUtils;


/*
 * 自定义SessionDao：需要继承CachingSessionDAO，该类是一个抽象类，因此需要实现其中的抽象方法
 * 因为继承了 CachingSessionDAO；所以在读取session时会先查缓存中是否存在，如果找不到才到数据库中查找。
 */
public class MySessionDao extends CachingSessionDAO{
    
    private JdbcTemplate jdbcTemplate = JdbcTemplateUtils.jdbcTemplate();
    
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);    //父类的方法
        assignSessionId(session, sessionId);
        String sql = "insert into sessions(id, session) values(?, ?)";
        jdbcTemplate.update(sql, sessionId, SerializableUtils.serialize(session));
        return null;
    }

    @Override
    protected void doUpdate(Session session) {
        //如果会话过期或者停止，那就没必要再更新了
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
            return;
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
        List<String> sessionStrList = jdbcTemplate.queryForList(sql, String.class, sessionId);
        if(sessionStrList.size() == 0) {
            return null;
        }
        return SerializableUtils.deserialize(sessionStrList.get(0));
    }

}
