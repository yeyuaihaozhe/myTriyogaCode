package com.sd.wq.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comba.util.CombaCommonUtil;
import com.sd.wq.client.CombaWsClient;

@Controller
@RequestMapping(value = "/wq/kaoqin")
public class KaoqinController extends AbstractController{
	@RequestMapping("/kaoqin1")
	public String kaoqin1(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String agentId = request.getParameter("agentId");
		String code = request.getParameter("code");
		String userInfos = CombaWsClient.getKaoQinUser(code,agentId);
		String msg="打卡异常";
		if(null == userInfos){
			userInfos = "read fail!";
			msg+="，未知异常";
			
		}else{
			Map<String, Object> map = CombaCommonUtil.toMap(userInfos);
			request.setAttribute("userId", CombaCommonUtil.getValueString(map, "userId"));
			request.setAttribute("userNum", CombaCommonUtil.getValueString(map, "userNum"));
			request.setAttribute("userTrueName", CombaCommonUtil.getValueString(map, "userTrueName"));
			request.setAttribute("latitude", CombaCommonUtil.getValueString(map, "latitude"));
			request.setAttribute("longitude", CombaCommonUtil.getValueString(map, "longitude"));
			request.setAttribute("address", CombaCommonUtil.getValueString(map, "address"));
			if(CombaCommonUtil.isEmpty(map.get("userTrueName"))){
				msg+="，无法获取用户姓名";
			}else if(CombaCommonUtil.isEmpty(map.get("address"))){
				msg+="，无法获取打卡地址";
			}else{
				msg="打卡成功";
			}
		}
		request.setAttribute("msg", msg);
		return "kaoqinzs/kaoqin";
	}
	@RequestMapping("/myinfo")
	public String myinfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String scheme = request.getScheme();
		System.out.println("scheme="+scheme);
		System.out.println("header###b#######");
		Enumeration headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()){
			Object nextElement = headerNames.nextElement();
			String h=nextElement.toString();
			String header = request.getHeader(h);
			System.out.println(h+"="+header);
		}
		System.out.println("header##e########");
		
		System.out.println("attribute###b#######");
		Enumeration attributeNames = request.getAttributeNames();
		while(attributeNames.hasMoreElements()){
			Object nextElement = attributeNames.nextElement();
			String h=nextElement.toString();
			Object attribute = request.getAttribute(h);
			System.out.println(h+"="+attribute);
		}
		System.out.println("attribute###e#######");
		
		
		
		String agentId = request.getParameter("agentId");
		String code = request.getParameter("code");
		String userInfos = CombaWsClient.getMyinfo(code,agentId);
		String msg="打卡异常";
		if(null == userInfos){
			userInfos = "read fail!";
			msg+="，未知异常";
			
		}else{
			Map<String, Object> map = CombaCommonUtil.toMap(userInfos);
			request.setAttribute("userId", CombaCommonUtil.getValueString(map, "userId"));
			request.setAttribute("userNum", CombaCommonUtil.getValueString(map, "userNum"));
			request.setAttribute("userTrueName", CombaCommonUtil.getValueString(map, "userTrueName"));
			request.setAttribute("latitude", CombaCommonUtil.getValueString(map, "latitude"));
			request.setAttribute("longitude", CombaCommonUtil.getValueString(map, "longitude"));
			request.setAttribute("address", CombaCommonUtil.getValueString(map, "address"));
			if(CombaCommonUtil.isEmpty(map.get("userTrueName"))){
				msg+="，无法获取用户姓名";
			}else if(CombaCommonUtil.isEmpty(map.get("address"))){
				msg+="，无法获取打卡地址";
			}else{
				msg="打卡成功";
			}
		}
		request.setAttribute("msg", msg);
		return "kaoqinzs/myinfo";
	}
	@RequestMapping("/kaoqinList")
	public String kaoqinList(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String agentId = request.getParameter("agentId");
		String code = request.getParameter("code");
		String userInfos = CombaWsClient.getMyinfo(code,agentId);
		Map<String, Object> map = CombaCommonUtil.toMap(userInfos);
		String userNum=CombaCommonUtil.getValueString(map, "userNum");
		String sql="select * from comba_kaoqin_info where usernum='"+userNum+"' order by createtime desc";
		System.out.println(sql);
		List<Map<String, Object>> query = getRdmTestJDBC().query(sql);
		request.setAttribute("data", query);
		System.out.println("data size="+query.size());
		return "kaoqinzs/kaoqinList";
	}
}