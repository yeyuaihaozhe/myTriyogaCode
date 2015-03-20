package com.sd.wq.client;

import java.util.List;
import java.util.Map;

import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpJsapiSignature;
import me.chanjar.weixin.cp.bean.WxCpUser;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.comba.util.CombaCommonUtil;
import com.sd.wq.conf.SystemConfig;
import com.sd.wq.service.CombaWQWebService;

public class CombaWsClient {
	private static CombaWQWebService ws;
	static{
		initWS();
	}
	private static void initWS(){
		initWQWS();
	}
	private static void initWQWS(){
		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
		factoryBean.setServiceClass(CombaWQWebService.class);
		factoryBean.setAddress(SystemConfig.SYSTEM_WS_URL);
//		factoryBean.setAddress("http://192.168.0.144:8080/WQ/ws/CombaWQWebService");
//		factoryBean.setAddress("http://192.168.8.174:8080/WQ/ws/CombaWQWebService");
		ws = (CombaWQWebService) factoryBean.create();
		
		System.out.println("ws = "+ws);
	}
	public static String handleMessage(Map<String,Object> input,String data){
		String inputStr=CombaCommonUtil.jsonParseString(input);
		return ws.handleMessage(inputStr,data);
	}
	/**
	 * 获取当前用户经纬度
	 * @param sMsg
	 * @return
	 */
	public static String getLocation(String sMsg){
		return ws.getLocation(sMsg);
	}
	public static String getKaoQinUser(String code,String agentId){
		System.out.println("获取考勤菜单当前用户"+code+" agentId = "+agentId);
		return ws.getKaoQinUser(code,agentId);
	}
	public static String getMyinfo(String code,String agentId){
		System.out.println("获取考勤菜单当前用户"+code+" agentId = "+agentId);
		return ws.getMyinfo(code,agentId);
	}
	public static void sendMultyArticleMsg(String agentId,String title,String description,String toUser,
			String toParty,String toTag,String url,int imageType){
		 ws.sendMultyArticleMsg(agentId,title, description, toUser, toParty, toTag, url, imageType);	 
	}
	public static boolean addUser(WxCpUser user){
		return ws.addWxCpUser(user);
	}
	public static boolean updateUser(WxCpUser user){
		return ws.updateWxCpUser(user);
	}
	public static boolean deleteUser(String userId){
		return ws.deleteWxCpUser(userId);
	}
	public static WxCpUser getUser(String userId){
		return ws.getWxCpUser(userId);
	}
	public static List<WxCpUser> departGetUsers(int departId,
			boolean fetchChild,int status){
		return ws.departGetUsers(departId, fetchChild, status);
	}
	public static int addWxCpDepart(WxCpDepart depart) {
		return ws.addWxCpDepart(depart);
	}
	public static boolean updateWxCpDepart(WxCpDepart depart) {
		return ws.updateWxCpDepart(depart);
	}
	public static boolean deleteWxCpDepart(int deptId) {
		return ws.deleteWxCpDepart(deptId);
	}
	public static List<WxCpDepart> getWxCpAllDepart() {
		return ws.getWxCpAllDepart();
	}
	public static boolean userInvite(String userId,String inviteTips){
		return ws.userInvite(userId,inviteTips);
	}
	public static WxCpJsapiSignature createJsapiSignature(String url){
		return ws.createJsapiSignature(url);
	}
}
