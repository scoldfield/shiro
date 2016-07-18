package com.cmcc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.dao.UserRunAsDao;

@Service
public class UserRunAsServiceImpl implements UserRunAsService {

    @Autowired
    private UserRunAsDao userRunAsDao;
    
    @Override
    public void grantRunAs(Long fromUserId, Long toUserId) {
        userRunAsDao.grantRunAs(fromUserId, toUserId);
    }

    @Override
    public void revokeRunAs(Long fromUserId, Long toUserId) {
        userRunAsDao.revokeRunAs(fromUserId, toUserId);
    }

    @Override
    public boolean exists(Long fromUserId, Long toUserId) {
        return userRunAsDao.exists(fromUserId, toUserId);
    }

    @Override
    public List<Long> findFromUserIds(Long toUserId) {
        return userRunAsDao.findFromUserIds(toUserId);
    }

    @Override
    public List<Long> findToUserIds(Long fromUserId) {
        return userRunAsDao.findToUserIds(fromUserId);
    }
}
