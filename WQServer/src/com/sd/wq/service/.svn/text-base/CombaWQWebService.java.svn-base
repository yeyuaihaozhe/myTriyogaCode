package com.sd.wq.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpJsapiSignature;
import me.chanjar.weixin.cp.bean.WxCpUser;

@WebService
public interface CombaWQWebService {
	/**
	 * 处理消息
	 * @param inputStr 输入参数
	 * @param data  数据
	 * @return
	 */
	public String handleMessage(
			@WebParam(name = "inputStr") String inputStr,
			@WebParam(name = "data") String data);
	/**
	 * 读取经纬度
	 * @param data
	 * @return
	 */
	public String getLocation(
			@WebParam(name = "data") String data);
	/**
	 * 读取经纬度
	 * @param userName
	 * @return
	 */
	public String getLocationByUserName(
			@WebParam(name = "userName") String userName);
	/**
	 * 获取考勤菜单当前用户
	 * @return
	 */
	public String getKaoQinUser(
			@WebParam(name = "code") String code,
			@WebParam(name = "agentId") String agentId);
	
	public String getMyinfo(
			@WebParam(name = "code") String code,
			@WebParam(name = "agentId") String agentId);
	/**
	 * 发送多图文消息
	 * @param title
	 * @param description
	 * @param toUser 	非必填，UserID列表（消息接收者，多个接收者用‘|’分隔）。特殊情况：指定为@all，则向关注该企业应用的全部成员发送
	 * @param toParty	非必填，PartyID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数
	 * @param toTag		非必填，TagID列表，多个接受者用‘|’分隔。当touser为@all时忽略本参数
	 * @param url
	 * @param imageType
	 */
	public void sendMultyArticleMsg(
			@WebParam(name = "agentId") String agentId,
			@WebParam(name = "title") String title,
			@WebParam(name = "description") String description,
			@WebParam(name = "toUser") String toUser,
			@WebParam(name = "toParty") String toParty,
			@WebParam(name = "toTag") String toTag,
			@WebParam(name = "url") String url,
			@WebParam(name = "imageType") int imageType);
	/**
	 * 增加企业号成员
	 * @param user
	 * @return
	 */
	public boolean addWxCpUser( 
			@WebParam(name = "user") WxCpUser user);
	/**
	 * 更新企业号成员
	 * @param user
	 * @return
	 */
	public boolean updateWxCpUser(
			@WebParam(name = "user") WxCpUser user);
	/**
	 * 删除企业号成员
	 * @param userId
	 * @return
	 */
	public boolean deleteWxCpUser(
			@WebParam(name = "user") String userId);
	/**
	 * 根据成员id获取企业号成员
	 * @param userId
	 * @return
	 */
	public WxCpUser getWxCpUser(
			@WebParam(name = "user") String userId);
	/**
	 * 获取企业号部门下的成员
	 * @param departId
	 * @param fetchChild
	 * @param status
	 * @return
	 */
	public List<WxCpUser> departGetUsers(
			@WebParam(name = "departId") int departId,
			@WebParam(name = "fetchChild") boolean fetchChild,
			@WebParam(name = "status") int status);
	/**
	 * 增加企业号部门
	 * @param depart
	 * @return
	 */
	public int addWxCpDepart(
			@WebParam(name = "depart") WxCpDepart depart);
	/**
	 * 更新企业号部门
	 * @param depart
	 * @return
	 */
	public boolean updateWxCpDepart(
			@WebParam(name = "depart") WxCpDepart depart);
	/**
	 * 删除企业号部门
	 * @param deptId
	 * @return
	 */
	public boolean deleteWxCpDepart(
			@WebParam(name = "deptId") int deptId);
	/**
	 * 根据部门id获取企业号部门信息
	 * @return
	 */
	public List<WxCpDepart> getWxCpAllDepart();
	/**
	 * 邀请成员关注
	 * @param deptId
	 * @return
	 */
	public boolean userInvite(
			@WebParam(name = "userId") String userId,
			@WebParam(name = "inviteTips") String inviteTips);
	/**
	 * 获取微信JsApiSignature
	 * @param url
	 * @return
	 */
	public WxCpJsapiSignature createJsapiSignature(
			@WebParam(name = "url") String url);
}
