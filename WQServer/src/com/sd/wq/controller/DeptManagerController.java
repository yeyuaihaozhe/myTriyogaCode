package com.sd.wq.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.cp.bean.WxCpDepart;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comba.util.CombaCommonUtil;
import com.comba.wq.dao.jdbc.JdbcDAO;
import com.comba.wq.util.SpringContextUtil;
import com.sd.wq.client.CombaWsClient;

@Controller
@RequestMapping(value = "/wq/deptmanager")
public class DeptManagerController {
	@RequestMapping("/addDepart")
	public void addDepart(HttpServletRequest request,
			HttpServletResponse response){
		WxCpDepart depart = new WxCpDepart();
		String name = request.getParameter("name");
		String parentId = request.getParameter("parentId");
		try {
			name = new String(name.getBytes("ISO8859-1"),"UTF-8");
			depart.setName(name);
			depart.setParentId(Integer.valueOf(parentId));
			int departId = CombaWsClient.addWxCpDepart(depart);
			if( departId != -1){
				System.out.println("create depart success"+"  departId = "+departId);
			}else{
				System.out.println("create depart fail");
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("create depart fail");
		}
	} 
	@RequestMapping("/updateDepart")
	public void updateDepart(HttpServletRequest request,
			HttpServletResponse response){
		WxCpDepart depart = getDeptInfo(request);
		String name = depart.getName();
		try {
			name = new String(name.getBytes("ISO8859-1"),"UTF-8");
			depart.setName(name);
			if(CombaWsClient.updateWxCpDepart(depart)){
				System.out.println("update depart success");
			}else{
				System.out.println("update depart fail");
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("update depart fail");
		}
	}
	@RequestMapping("/deleteDepart")
	public void deleteDepart(HttpServletRequest request,
			HttpServletResponse response){
		String id = request.getParameter("id");
		if(CombaWsClient.deleteWxCpDepart(Integer.valueOf(id))){
			System.out.println("delete depart success");
		}else{
			System.out.println("delete depart fail");
		}
	}
	@RequestMapping("/getAllDepart")
	public void getAllDepart(HttpServletRequest request,
			HttpServletResponse response){
		List<WxCpDepart> departs = CombaWsClient.getWxCpAllDepart();
		if(null == departs){
			System.out.println("get departs fail");
		}else{
			System.out.println("get departs success");
			System.out.println("departs = "+CombaCommonUtil.jsonParseString(departs));
		}
	}
	@RequestMapping("/saveDepartToDb")
	public void saveDepartToDb(HttpServletRequest request,
			HttpServletResponse response){
		List<WxCpDepart> departs = CombaWsClient.getWxCpAllDepart();
		if(null == departs){
			System.out.println("get departs fail");
		}else{
			System.out.println("get departs success");
			System.out.println("departs = "+CombaCommonUtil.jsonParseString(departs));
			saveWxDeptToDb(departs);
		}
	}
	private void saveWxDeptToDb(List<WxCpDepart> departs){
		if(departs.size()<= 0){
			return;
		}
		int size = departs.size();
		JdbcDAO wqJdbc=(JdbcDAO)SpringContextUtil.getBean("wqDb");
		for(int i=0; i<size; i++){
			WxCpDepart depart = departs.get(i);
			String name = depart.getName();			//部门名
			int id = depart.getId();			//部门Id
			int parentId = depart.getParentId();//父id
			if(isNotExist(id,wqJdbc)){
				String sql = "INSERT INTO COMBAWQ_DPT T (T.ID,T.NAME,T.P_ID)VALUES(?,?,?)";
				wqJdbc.update(sql, id,name,parentId);
			}
		}
	}
	private boolean isNotExist(int id,JdbcDAO jdbc){
		String sql = "SELECT T.ID FROM COMBAWQ_DPT T WHERE T.ID= ? ";
		List<Map<String, Object>> query = jdbc.query(sql, String.valueOf(id));
		if(query.size() > 0){
			return false;
		}
		return true;
	}
	private WxCpDepart getDeptInfo(HttpServletRequest request){
		WxCpDepart depart = new WxCpDepart();
		depart.setId(Integer.valueOf(request.getParameter("id")));
		System.out.println("id = "+depart.getId());
		depart.setName(request.getParameter("name"));
		System.out.println("name = "+depart.getName());
		depart.setParentId(Integer.valueOf(request.getParameter("parentId")));
		System.out.println("parentId = "+depart.getParentId());
		return depart;
	}
}
