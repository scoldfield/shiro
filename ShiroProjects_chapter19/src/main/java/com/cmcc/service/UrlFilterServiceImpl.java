package com.cmcc.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmcc.dao.UrlFilterDao;
import com.cmcc.entity.UrlFilter;

@Service
public class UrlFilterServiceImpl implements UrlFilterService{

    @Autowired
    private UrlFilterDao urlFilterDao;
    @Autowired
    private ShiroFilterChainManager shiroFilterChainManager;
    
    @PostConstruct
    public void initFilterChain() {
        shiroFilterChainManager.initFilterChains(findAll());
    }
    
    @Override
    public UrlFilter createUrlFilter(UrlFilter urlFilter) {
        urlFilterDao.createUrlFilter(urlFilter);
        initFilterChain();
        return urlFilter;
    }

    @Override
    public UrlFilter updateUrlFilter(UrlFilter urlFilter) {
        urlFilterDao.updateUrlFilter(urlFilter);
        initFilterChain();
        return urlFilter;
    }

    @Override
    public void deleteUrlFilter(Long urlFilterId) {
        urlFilterDao.deleteUrlFilter(urlFilterId);
        initFilterChain();        
    }

    @Override
    public UrlFilter findOne(Long urlFilterId) {
        return urlFilterDao.findOne(urlFilterId);
    }

    @Override
    public List<UrlFilter> findAll() {
        return urlFilterDao.findAll();
    }

}
