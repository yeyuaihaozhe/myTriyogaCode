package com.sd.wq.conf;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.log4j.Logger;

public class SystemConfig {
	private static Logger logger=Logger.getLogger(SystemConfig.class);
	private static Properties properties = new Properties();
	
	public static String SYSTEM_DEFAULT_LANGUAGE="zh_CN";
	public static String SYSTEM_WS_URL="";
	
	public static String SYSTEM_PDF_IMAGER_FLODER="C:/";
	
	
	public static String SYSTEM_OAJOB1_BEGIN="8:00";
	public static String SYSTEM_OAJOB1_INTERVAL="60";
	
	/**
	 * 是否启动测试模式
	 */
	public static boolean isTest=false;
	
	static {
		reloadConfigData();
	}
	private static void setValue(){
		SYSTEM_DEFAULT_LANGUAGE= fixValue(SYSTEM_DEFAULT_LANGUAGE,"system.default.language");
		SYSTEM_WS_URL= fixValue(SYSTEM_WS_URL,"system.ws.url");
		
		SYSTEM_PDF_IMAGER_FLODER= fixValue(SYSTEM_PDF_IMAGER_FLODER,"system.pdf.image.floder");
		
		SYSTEM_OAJOB1_BEGIN= fixValue(SYSTEM_OAJOB1_BEGIN,"sys.job.oaJob1.begin");
		SYSTEM_OAJOB1_INTERVAL= fixValue(SYSTEM_OAJOB1_INTERVAL,"sys.job.oaJob1.intervalInSeconds");
	}
	private static String fixValue(String defaultValue,String key){
		String str=getText(key);
		if(null==str||"".equals(str.trim())){
			return defaultValue;
		}else{
			return str;
		}
	}
	/**
	 * 读取system-Config.properties中的配置
	 * 
	 * @param key 标签名
	 * @param objs {0}...参数的值
	 * @return
	 */
	private static String getText(String key, Object... objs) {
		String result = null;
		try {
			Object object = properties.get(key);
			if (null != object) {
				result = new String(object.toString().getBytes("ISO8859-1"),"UTF-8");
			}
			try {
				if (null!=result&&null != objs) {
					result = MessageFormat.format(result, objs);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	/**
	 * 重新加载配置
	 */
	public static void reloadConfigData(){
		InputStream resourceAsStream=null;
		try {
			resourceAsStream = SystemConfig.class.getResourceAsStream("/system-Config.properties");
			properties.load(resourceAsStream);
			resourceAsStream.close();
			
			setValue();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} finally {
			try {
				if(null!=resourceAsStream){
					resourceAsStream.close();
				}
			} catch (Exception e) {
				logger.error("网络异常关闭流失败！", e);
			}
		}
	}

}
