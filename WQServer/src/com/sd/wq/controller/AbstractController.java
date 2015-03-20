package com.sd.wq.controller;

import com.comba.wq.dao.jdbc.JdbcDAO;
import com.comba.wq.util.SpringContextUtil;


public abstract class AbstractController {
	private JdbcDAO rdmTestJDBC;
	private JdbcDAO oaJDBC;
	protected JdbcDAO getOaJDBC(){
		if(null==rdmTestJDBC){
			oaJDBC=(JdbcDAO)SpringContextUtil.getBean("oaDb");
		}
		return oaJDBC;
	}
	protected JdbcDAO getRdmTestJDBC(){
		if(null==rdmTestJDBC){
			rdmTestJDBC=(JdbcDAO)SpringContextUtil.getBean("rdmTestDb");
		}
		return rdmTestJDBC;
	}
}
