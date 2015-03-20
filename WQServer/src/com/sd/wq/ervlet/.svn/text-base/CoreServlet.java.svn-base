package com.sd.wq.ervlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.comba.util.CombaCommonUtil;
import com.sd.wq.aes.AesException;
import com.sd.wq.aes.WXBizMsgCrypt;
import com.sd.wq.cache.MsgCache;
import com.sd.wq.client.CombaWsClient;
import com.sd.wq.service.MessageUtil;

/**
 * 核心请求处理类
 * 
 * @author heqian
 * 
 */
public class CoreServlet extends HttpServlet {
	private Logger logger=Logger.getLogger(CoreServlet.class);
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 设置编码
		System.out.println("进洞2222");
		response.setContentType("text/html;charset=utf-8");
		// response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		Map<String, Object> map = new HashMap<String, Object>();
		Set<String> keySet = request.getParameterMap().keySet();
		for (String s : keySet) {
			String str = request.getParameter(s);
			map.put(s, str);
		}
		String rest = CombaCommonUtil.jsonParseString(map);
		System.out.println(rest);
		try {
			WXBizMsgCrypt wxcpt = MessageUtil.getWxcpt();

			String sVerifyMsgSig = URLDecoder.decode(
					request.getParameter("msg_signature"), "utf-8");
			String sVerifyTimeStamp = URLDecoder.decode(
					request.getParameter("timestamp"), "utf-8");
			String sVerifyNonce = URLDecoder.decode(
					request.getParameter("nonce"), "utf-8");
			String sVerifyEchoStr = URLDecoder.decode(
					request.getParameter("echostr"), "utf-8");
			PrintWriter out = response.getWriter();
			String sEchoStr; // 需要返回的明文
			try {
				sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp,
						sVerifyNonce, sVerifyEchoStr);
				System.out.println("verifyurl echostr: " + sEchoStr);
				
				// 验证URL成功，将sEchoStr返回
				out.print(sEchoStr);
				out.close();
				out = null;
			} catch (Exception e) {
				// 验证URL失败，错误原因请查看异常
				e.printStackTrace();
			}

		} catch (AesException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 获得请求参数
		System.out.println("进洞333");
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
			String sMsg = wxcpt.DecryptMsg(reqMsgSig, timestamp,nonce, sReqData);

			String encryptMsg="";
			try{
				Map<String,Object> input=new HashMap<String,Object>();
				Set<String> keySet = request.getParameterMap().keySet();
				for(String s:keySet){
					String str= request.getParameter(s);
					input.put(s,str);
				}
				String remoteAddr = request.getRemoteAddr();
				System.out.println("remoteAddr="+remoteAddr);
				System.out.println("request.getRequestedSessionId()="+request.getRequestedSessionId());
				MsgCache.setMsg(remoteAddr, sMsg);
				System.out.println("测试位置AA");
				System.out.println("sMsg = "+sMsg);
				encryptMsg = CombaWsClient.handleMessage(input,sMsg);
				System.out.println("encryptMsg = "+encryptMsg);
				
			}catch(Exception e){
				logger.error(e.getMessage(),e);
			}
			
			// 响应消息
			PrintWriter out = response.getWriter();
			out.print(encryptMsg);
			out.close();
		} catch (AesException e1) {
			sEchoStr = "ERR: " + e1.getCode() + "\n\n";
			logger.error(e1.getMessage(),e1);
		} catch (Exception e) {
			sEchoStr = "ERR: " + AesException.ParseXmlError + "\n\n";
			logger.error(e.getMessage(),e);
		}
		response.getWriter().print(sEchoStr);
	}
}