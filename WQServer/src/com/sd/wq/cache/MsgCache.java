package com.sd.wq.cache;

import java.util.HashMap;
import java.util.Map;

public class MsgCache {
	private static Map<String,String> msgMap=new HashMap<String,String>();
	public static void setMsg(String id,String msg){
		msgMap.put(id, msg);
	}
	/**
	 *读取经纬度
	 * @param userName
	 * @return null or longitude+":"+latitude
	 */
	public static String getMsg(String id){
		return msgMap.get(id);
	}
}
