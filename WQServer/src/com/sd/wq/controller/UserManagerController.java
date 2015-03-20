package com.sd.wq.controller;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.cp.bean.WxCpUser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comba.util.CombaCommonUtil;
import com.comba.wq.dao.jdbc.JdbcDAO;
import com.comba.wq.util.SpringContextUtil;
import com.sd.wq.client.CombaWsClient;

@Controller
@RequestMapping(value = "/wq/usermanager")
public class UserManagerController extends AbstractController{
	@RequestMapping("/inviteUser")
	public void inviteUser(HttpServletRequest request,
			HttpServletResponse response){
		String userId = request.getParameter("userId");
		System.out.println("userId = "+userId);
		String inviteTips = request.getParameter("inviteTips");
		try {
			inviteTips = new String(inviteTips.getBytes("ISO8859-1"),"UTF-8");
			if(CombaWsClient.userInvite(userId,inviteTips)){
				System.out.println("Invite user success");
			}else{
				System.out.println("Invite user fail");
			}
		} catch (Exception e) {
			System.out.println("Invite user fail");
		}
	}
	@RequestMapping("/addUser")
	public void addUser(HttpServletRequest request,
			HttpServletResponse response){
		WxCpUser user = getUserInfo(request);
		String trueName = user.getName();
		String position = user.getPosition();
		//解决中文乱码问题，将中文名称转换为ISO8859-1编码
		try {
			trueName = new String(trueName.getBytes("ISO8859-1"),"UTF-8");
			position = new String(position.getBytes("ISO8859-1"),"UTF-8");
			user.setName(trueName);
			user.setPosition(position);
			System.out.println("username = "+user.getName());
			if(CombaWsClient.addUser(user)){
				System.out.println("create user success");
			}else{
				System.out.println("create user fail");
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("create user fail");
		}
	} 
	@RequestMapping("/updateUser")
	public void updateUser(HttpServletRequest request,
			HttpServletResponse response){
		WxCpUser user = getUserInfo(request);
		String trueName = user.getName();
		String position = user.getPosition();
		try {
			trueName = new String(trueName.getBytes("ISO8859-1"),"UTF-8");
			position = new String(position.getBytes("ISO8859-1"),"UTF-8");
			user.setName(trueName);
			user.setPosition(position);
			if(CombaWsClient.updateUser(user)){
				System.out.println("update user success");
			}else{
				System.out.println("update user fail");
			}
		} catch (UnsupportedEncodingException e) {
			System.out.println("update user fail");
		}
	}
	@RequestMapping("/deleteUser")
	public void deleteUser(HttpServletRequest request,
			HttpServletResponse response){
		String userId = request.getParameter("userId");
		if(CombaWsClient.deleteUser(userId)){
			System.out.println("delete user success");
		}else{
			System.out.println("delete user fail");
		}
	}
	@RequestMapping("/getUser")
	public void getUser(HttpServletRequest request,
			HttpServletResponse response){
		String userId = request.getParameter("userId");
		WxCpUser userGet = CombaWsClient.getUser(userId);
		if(null == userGet){
			System.out.println("get user fail");
		}else{
			System.out.println("get user success");
			System.out.println("userGet = "+CombaCommonUtil.jsonParseString(userGet));
		}
	}
	@RequestMapping("/departGetUsers")
	public void departGetUsers(HttpServletRequest request,
			HttpServletResponse response){
		System.out.println("into deptget ");
		
		Map<String, Object> map = getRequestInfo(request);
		int departId = (Integer)map.get("departId");
		boolean fetchChild = false;
		if(1 == CombaCommonUtil.getValueInt(map, "fetchChild")){
			fetchChild = true;
		}
		int status = (Integer)map.get("status");
		
		List<WxCpUser> users = CombaWsClient.departGetUsers(departId,fetchChild,status);
		System.out.println("users = "+CombaCommonUtil.jsonParseString(users));
		if(users.size() < 0){
			System.out.println("get depart users fail");
		}else{
			System.out.println("get depart users success");
//			saveUserToDb(users);
		}
	}
	@RequestMapping("/saveWxUserToDb")
	public void saveWxUserToDb(HttpServletRequest request,
			HttpServletResponse response){
		System.out.println("into deptget ");
		
		Map<String, Object> map = getRequestInfo(request);
		int departId = (Integer)map.get("departId");
		boolean fetchChild = false;
		if(1 == CombaCommonUtil.getValueInt(map, "fetchChild")){
			fetchChild = true;
		}
		int status = (Integer)map.get("status");
		
		List<WxCpUser> users = CombaWsClient.departGetUsers(departId,fetchChild,status);
		System.out.println("users = "+CombaCommonUtil.jsonParseString(users));
		if(users.size() < 0){
			System.out.println("get depart users fail");
		}else{
			System.out.println("get depart users success");
			saveUserToDb(users);
		}
	}
	private void saveUserToDb(List<WxCpUser> users){
		if(users.size()<= 0){
			return;
		}
		int size = users.size();
		JdbcDAO wqJdbc=(JdbcDAO)SpringContextUtil.getBean("wqDb");
		for(int i=0; i<size; i++){
			WxCpUser user = users.get(i);
			String truename = user.getName();			//中文名
			String mobile = user.getMobile();			//手机号码
			String email = user.getEmail();				//邮箱
			Integer[] departIds = user.getDepartIds();	//部门Id列表
			String position = user.getPosition();		//职位
			String userId = user.getUserId();			//用户id
			String weiXinId = user.getWeiXinId();		//微信号
			String gender = user.getGender();			//性别
			if(isNotExist(userId,wqJdbc)){
				String sql = "INSERT INTO COMBAWQ_USER T (T.CODE,T.TRUENAME,T.DPT_ID,T.MOBILE,T.EMAIL,T.WEIXINID,T.POSITION,T.GENDER)" +
						"VALUES(?,?,?,?,?,?,?,?)";
				wqJdbc.update(sql,userId,truename,departIds[0],mobile,email,weiXinId,position,gender);
			}
		}
	}
	private boolean isNotExist(String id,JdbcDAO jdbc){
		String sql = "SELECT T.CODE FROM COMBAWQ_USER T WHERE T.CODE= ? ";
		List<Map<String, Object>> query = jdbc.query(sql, String.valueOf(id));
		if(query.size() > 0){
			return false;
		}
		return true;
	}
	private Map<String,Object>getRequestInfo(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		String departId = request.getParameter("departId");
		String fetchChild = request.getParameter("fetchChild");
		String status = request.getParameter("status");
		map.put("departId", Integer.valueOf(departId));
		map.put("fetchChild", fetchChild);
		map.put("status", Integer.valueOf(status));
		return map;
	}
	private WxCpUser getUserInfo(HttpServletRequest request){
		System.out.println("into wq AddWxCpUser !!");
		String trueName = request.getParameter("truename");
		System.out.println("trueName = "+trueName);
		String combaId = request.getParameter("combaId");
		System.out.println("combaid = "+combaId);
		String phone = request.getParameter("phone");
		System.out.println("phone = "+phone);
		String email = request.getParameter("email");
		System.out.println("email = "+email);
		String departmentId = request.getParameter("department");
		System.out.println("departmentId = "+departmentId);
		String weixinId = request.getParameter("weixinId");
		System.out.println("weixinId = "+weixinId);
		String position = request.getParameter("position");
		System.out.println("position = "+position);
		WxCpUser user = new WxCpUser();
		user.setName(trueName);
		user.setUserId(combaId);
		user.setMobile(phone);
		user.setEmail(email);
		user.setWeiXinId(weixinId);
		user.setPosition(position);
		String[] depts = departmentId.split(",");

		Integer[] deptId = new Integer[depts.length];
		for(int i=0; i<depts.length; i++){
			deptId[i] = Integer.parseInt(depts[i]);
		}
		System.out.println("deptId = "+deptId);
		user.setDepartIds(deptId);
		return user;
	}
}
