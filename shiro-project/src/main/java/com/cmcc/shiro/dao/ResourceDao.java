package com.cmcc.shiro.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cmcc.basic.dao.BaseDao;
import com.cmcc.shiro.model.Resource;

@Repository("resourceDao")
public class ResourceDao extends BaseDao<Resource> implements IResourceDao {

	public List<Resource> listResource() {
		return super.list("from Resource");
	}
	
}
