package com.sd.wq.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comba.util.CombaCommonUtil;
import com.sd.wq.aes.AesException;
import com.sd.wq.aes.WXBizMsgCrypt;
import com.sd.wq.service.MessageUtil;
/**
 * 注解方式打开链接
 * 
 * @author Sunlight
 *
 */
@Controller
@RequestMapping(value = "/wq/tool")
public class CoreController {
	@RequestMapping("/link")
	public void link(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 获得请求参数
				String reqMsgSig = request.getParameter("msg_signature");
				String timestamp = request.getParameter("timestamp");
				String nonce = request.getParameter("nonce");
				// 获得post提交的数据
				BufferedReader br = new BufferedReader(new InputStreamReader(
						request.getInputStream()));
				StringBuilder sbuff = new StringBuilder();
				String tmp = null;
				while ((tmp = br.readLine()) != null) {
					sbuff.append(tmp);
				}
				System.out.println("reqMsgSig="+reqMsgSig+" timestamp="+timestamp+" nonce="+nonce);
				System.out.println(sbuff);
				String sReqData = sbuff.toString();
				String sEchoStr = null;
				try {
					WXBizMsgCrypt wxcpt = MessageUtil.getWxcpt();
					String msg = wxcpt.DecryptMsg(reqMsgSig, timestamp,nonce, sReqData);
					Map<String, String> requestMap = MessageUtil.parseXml(msg);// xml请求解析
					
					// 响应消息
					PrintWriter out = response.getWriter();
					out.print(CombaCommonUtil.jsonParseString(requestMap)+"<a href='http://www.comba.com.cn'>京信</a>");
					out.close();
				} catch (AesException e1) {
					sEchoStr = "ERR: " + e1.getCode() + "\n\n";
					e1.printStackTrace();
				} catch (Exception e) {
					sEchoStr = "ERR: " + AesException.ParseXmlError + "\n\n";
					e.printStackTrace();
				}
				response.getWriter().print(sEchoStr+"<a href='http://www.comba.com.cn'><font size=50>京信</font></a>");
	}
}