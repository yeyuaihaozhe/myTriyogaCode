package com.comba.wq.db.transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.comba.wq.dao.jdbc.JdbcDAO;

public class DbManager {
	public static final String DB_BASEDB="baseDb";
	public static final String DB_RDM="rdm";
	public static final String DB_PLM="plm";
	public static final String DB_TIMECARDRECORD="timeCardRecord";
	
	private Logger logger=Logger.getLogger(DbManager.class);
	private JdbcDAO baseDb;//在spring配置文件中注入
	private JdbcDAO rdm;//在spring配置文件中注入
	private JdbcDAO srm;//在spring配置文件中注入
	private JdbcDAO plm;//在spring配置文件中注入
	private JdbcDAO timeCardRecord;//在spring配置文件中注入
	private JdbcDAO sbox;//员工 信息库
	private JdbcDAO report;//在spring配置文件中注入
	private JdbcDAO oaDb;//在spring配置文件中注入
	private JdbcDAO wqDb;//在spring配置文件中注入
	
	public JdbcDAO getOaDb() {
		return oaDb;
	}
	public void setOaDb(JdbcDAO oaDb) {
		this.oaDb = oaDb;
	}
	public JdbcDAO getWqDb() {
		return wqDb;
	}
	public void setWqDb(JdbcDAO wqDb) {
		this.wqDb = wqDb;
	}
	public JdbcDAO getBaseDb() {
		return baseDb;
	}
	public void setBaseDb(JdbcDAO baseDb) {
		this.baseDb = baseDb;
	}
	public JdbcDAO getRdm() {
		return rdm;
	}
	public JdbcDAO getSrm() {
		return srm;
	}
	public void setSrm(JdbcDAO srm) {
		this.srm = srm;
	}
	public void setRdm(JdbcDAO rdm) {
		this.rdm = rdm;
	}
	
	public JdbcDAO getPlm() {
		return plm;
	}
	public void setPlm(JdbcDAO plm) {
		this.plm = plm;
	}
	public JdbcDAO getTimeCardRecord() {
		return timeCardRecord;
	}
	public void setTimeCardRecord(JdbcDAO timeCardRecord) {
		this.timeCardRecord = timeCardRecord;
	}
	public JdbcDAO getSbox() {
		return sbox;
	}
	public void setSbox(JdbcDAO sbox) {
		this.sbox = sbox;
	}
	public JdbcDAO getReport() {
		return report;
	}
	public void setReport(JdbcDAO report) {
		this.report = report;
	}
	/**
	 * 根据DB名获取数据库连接
	 * @param db
	 * @return
	 */
	public JdbcDAO getDbByName(String db){
		if(null==db){
			return getBaseDb();
		}
		
		if(DbManager.DB_PLM.equals(db.toLowerCase())){
			return getPlm();
		}else if(DbManager.DB_RDM.equals(db.toLowerCase())){
			return getRdm();
		}else if(DbManager.DB_TIMECARDRECORD.equals(db.toLowerCase())){
			return getTimeCardRecord();
		}else{
			return getBaseDb();
		}
	}
	/**
	 * 根据网元Id查找表TABLE_INFO 的信息
	 * @param tableId
	 * @return
	 */
	public Map<String,Object> getTableInfo(int tableId){
		String sql="select name from TABLE_INFO where id=?";
		logger.info(sql);
		Map<String,Object> map= baseDb.queryForMap(sql,tableId);
		if(null==map){
			map=new HashMap<String,Object>();
		}
		return map;
	}
	
	/**
	 * 查找当前表的列的情况
	 * @param tableId
	 * @return
	 */
	public List<Map<String,Object>> getColumns(int tableId){
		String sql="select * from COLUMN_INFO where table_id=?";
		logger.info(sql);
		List<Map<String,Object>> list= baseDb.queryForList(sql,tableId);
		if(null==list){
			list=new ArrayList<Map<String,Object>>();
		}
		return list;
	}
	/**
	 * 获取网元的列配置
	 * @param tableId
	 * @return
	 */
	public List<Map<String,Object>> getGridConfig(int GridId){
		
		String sql="select * from COLUMN_INFO where table_id=?";
		logger.info(sql);
		List<Map<String,Object>> list= baseDb.queryForList(sql,GridId);
		if(null==list){
			list=new ArrayList<Map<String,Object>>();
		}
		return list;
	}

	
}
