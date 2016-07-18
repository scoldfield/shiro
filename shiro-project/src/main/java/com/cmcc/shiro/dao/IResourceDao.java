package com.cmcc.shiro.dao;

import java.util.List;

import com.cmcc.basic.dao.IBaseDao;
import com.cmcc.shiro.model.Resource;


public interface IResourceDao extends IBaseDao<Resource>{
	public List<Resource> listResource();
}
