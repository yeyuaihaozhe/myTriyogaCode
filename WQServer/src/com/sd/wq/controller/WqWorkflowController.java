package com.sd.wq.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comba.util.CombaCommonUtil;
import com.sd.wq.client.CombaWsClient;

@Controller
@RequestMapping(value = "/wq/wf")
public class WqWorkflowController extends AbstractController{
	@RequestMapping("/myWF")
	public String myWF(HttpServletRequest request,
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
			
		}
		request.setAttribute("msg", msg);
		return "kaoqinzs/kaoqin";
	}
}
