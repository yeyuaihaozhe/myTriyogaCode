package com.sd.wq.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.comba.util.CombaCommonUtil;
import com.sd.wq.aes.AesException;
import com.sd.wq.aes.WXBizMsgCrypt;
import com.sd.wq.model.Article;
import com.sd.wq.model.MusicMessage;
import com.sd.wq.model.NewsMessage;
import com.sd.wq.model.TextMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 消息工具类
 * 
 * @author sunlight
 *
 */
public class MessageUtil {
	private static Logger logger=Logger.getLogger(MessageUtil.class);
	private static String sCorpID = "wxbafe2b35c75e7daa";
	private static String backFromUserName="wxbafe2b35c75e7daa";
	private static String sToken = "nAU5v9EL";
	private static String sEncodingAESKey = "WsP9FetExvevxmzkJWl4ddc5BOSbDb6PN9nv3fnPkik";
	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MESSAGE_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";

	/**
	 * 请求消息类型：推送
	 */
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";

	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(String msg)
			throws Exception {
		// 将解析结果存储在HashMap中
		Map<String, String> map = new HashMap<String, String>();

		// 从request中取得输入流
		InputStream inputStream = new ByteArrayInputStream(msg.getBytes("UTF-8"));
		
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();

		// 遍历所有子节点
		for (Element e : elementList)
			map.put(e.getName(), e.getText());

		// 释放资源
		inputStream.close();
		inputStream = null;

		return map;
	}

	/**
	 * 文本消息对象转换成xml
	 * 
	 * @param textMessage
	 *            文本消息对象
	 * @return xml
	 */
	public static String textMessageToXml(TextMessage textMessage) {
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}

	/**
	 * 音乐消息对象转换成xml
	 * 
	 * @param musicMessage
	 *            音乐消息对象
	 * @return xml
	 */
	public static String musicMessageToXml(MusicMessage musicMessage) {
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}

	/**
	 * 图文消息对象转换成xml
	 * 
	 * @param newsMessage
	 *            图文消息对象
	 * @return xml
	 */
	public static String newsMessageToXml(NewsMessage newsMessage) {
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);
	}

	/**
	 * 扩展xstream，使其支持CDATA块
	 * 
	 * @date 2013-05-19
	 */
	private static XStream xstream = new XStream(new XppDriver() {
		@Override
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@Override
				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				@Override
				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
	
	
	public static WXBizMsgCrypt getWxcpt() throws AesException{
		return new WXBizMsgCrypt(sToken, sEncodingAESKey,sCorpID);
	}

	private static String getTextMsg(Map<String, String> requestMap) {
		// 发送方帐号（open_id）
		String fromUserName = requestMap.get("FromUserName");
		// 公众帐号
		String content = requestMap.get("Content");
		// 回复文本消息
		TextMessage textMessage = new TextMessage();
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(backFromUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setContent("Sunlight提示：您发送的是文本消息！内容是：" + content);
		return MessageUtil.textMessageToXml(textMessage);
	}
	private static String getEventLOCATION(Map<String, String> requestMap) {
//		for(String s:requestMap.keySet()){
//			System.out.println(s+"="+requestMap.get(s));
//		}
//		msgType==event
//				Event==LOCATION
//				事件类型LOCATION
//				FromUserName=2007000589
//				Event=LOCATION
//				Precision=40.000000
//				CreateTime=1418791339
//				Latitude=23.172237
//				Longitude=113.427971
//				AgentID=1
//				ToUserName=wxbafe2b35c75e7daa
//				MsgType=event
		// 发送方帐号（open_id）
		String Latitude = requestMap.get("Latitude");
		String Longitude = requestMap.get("Longitude");
		System.out.println("Latitude="+Latitude+" Longitude="+Longitude);
		String fromUserName = requestMap.get("FromUserName");
		// 回复文本消息
		TextMessage textMessage = new TextMessage();
		textMessage.setToUserName(fromUserName);
		textMessage.setFromUserName(backFromUserName);
		textMessage.setCreateTime(new Date().getTime());
		textMessage.setContent("打卡成功");
		return MessageUtil.textMessageToXml(textMessage);
	}
	public static String handlerMsg(String msg) {
		try {
			System.out.println(msg);
			
			Map<String, String> requestMap = MessageUtil.parseXml(msg);// xml请求解析
			
			// 消息类型
			String msgType = requestMap.get("MsgType");
			System.out.println("msgType=="+requestMap.get("MsgType"));
			System.out.println("Event=="+requestMap.get("Event"));
			// 文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				return getTextMsg(requestMap);
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				//TODO respContent = "Sunlight提示：您发送的是图片消息！";
				System.out.println("图片消息");
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				//TODO respContent = "Sunlight提示：您发送的是地理位置消息！"; 
				System.out.println("地理位置消息");
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				//TODO respContent = "Sunlight提示：您发送的是链接消息！";
				System.out.println("链接消息");
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				//TODO respContent = "Sunlight提示：您发送的是音频消息！";
				System.out.println("音频消息");
			}else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {// 事件推送
				// 事件类型
				String eventType = requestMap.get("Event");
				System.out.println("事件类型"+eventType);
				// 自定义菜单点击事件
				if (eventType.equalsIgnoreCase(MessageUtil.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应  
                    String eventKey = requestMap.get("EventKey");	
                    System.out.println("EventKey="+eventKey);
                  //TODO respContent = "Sunlight提示：您点击的菜单KEY是"+eventKey;
				}if ("LOCATION".equals(eventType)) {//经纬度
                    return getEventLOCATION(requestMap);
				}
			}else{
				logger.error("存在未处理包："+msg);
			}

			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return "处理异常了"+msg;
		}
		return "没进行处理？"+msg;
	}
	/**
	 * 程序中访问http数据接口,获取JSON结构
	 * @param urlStr
	 * @return
	 */
	public static String getURLContent(String urlStr) {
		/** 网络的url地址 */
		URL url = null;
		/**//** 输入流 */
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();
		try {
			url = new URL(urlStr);
			System.out.println("进入getUrlContent");
			in = new BufferedReader(new InputStreamReader(url.openStream(),
					"UTF-8"));
			String str = null;
			System.out.println("in = "+in);
			while ((str = in.readLine()) != null) {
				sb.append(str);
			}
		} catch (Exception ex) {

		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
			}
		}
		String result = sb.toString();
		System.out.println(result);
//		Map<String, Object> resultMap = CombaCommonUtil.toMap(result);
		
		//TODO 测试部分，完成测试请删除
//		for(String s:resultMap.keySet()){
//			System.out.println(s+"="+resultMap.get(s));
//		}
		return result;
	}
	private static void testReturnMsg(String sToken, String sEncodingAESKey){
		String sReqMsgSig="19a6bd09432a270dd1d8e1d1bed8db368b395fc1";
		String timestamp="1418807857";
		String nonce="116863065";
		String sReqData="<xml>";
		sReqData+="<ToUserName><![CDATA[wxbafe2b35c75e7daa]]></ToUserName>";
		sReqData+="<Encrypt><![CDATA[RIiNJT12k5rtoHbs9K6TDVsVGT4kPZITD3ZVtpJZQ3lnYMBnwzMbKlqCdPllmR6KQJ3OZlpdETTx/ZvLTHnnWojV40ZI/1cshzaYTebED3iYSe9EhwqxKLR/IgbKbIxAKZpXeet06wvJxPBqWSkxpBbbfUJHjlF3xhhn5gXITRL/u9PHaGraHomVrV6Qu2WKagPsNLxzdSjJDf3hBkYWfIubpbqyFvw1rCY7CZP87VFsqYuaSru2qv5RP4MpMw01ix6w2rdlVaK2jNgTyvINt9UHufaeSSR/8D1KVZmLA7iD4iaAlQVhyIWAdOcdvOVNAWVrWf9U8sBiF0719ZlQ+EGV/YwZqDNAe/4dfg5hXK/oKqwu1J3XlAaarxfDp3VEufptNcoAoDBNLKoJGLrIcrF8rYUjrOvdzR4NDZUwHfSo9blcKVIA+ZF8yNaKk3bqVNYFINOyr/LNTPAtfli2bnGQQ5t1Fpw2VDqidSQeHjyZQLRNnmHpKIDRinR5cGzKjzCLziWIb69CFgg8d7g7gSbQHMKKibZjUc8n3iX6maQ=]]></Encrypt>";
		sReqData+="<AgentID><![CDATA[1]]></AgentID>";
		sReqData+="</xml>";
		try {
			WXBizMsgCrypt wxcpt = MessageUtil.getWxcpt();
			String sMsg = wxcpt.DecryptMsg(sReqMsgSig, timestamp,nonce, sReqData);
			System.out.println("sMsg=" + sMsg);
			String msg = MessageUtil.handlerMsg(sMsg);
			// 调用核心业务类接收消息、处理消息
			String respMessage = MessageUtil.handlerMsg(msg);
			System.out.println("respMessage=" + respMessage);
			
			String encryptMsg = "";
			try {
				//加密回复消息
				encryptMsg = wxcpt.EncryptMsg(respMessage, timestamp, nonce);
				System.out.println(encryptMsg);
			} catch (AesException e) {
				e.printStackTrace();
			}
			
			// 响应消息
		} catch (AesException e1) {
		}
	}
	private void testResult(){
		String xml = "<xml><ToUserName><![CDATA[wxbafe2b35c75e7daa]]></ToUserName>";
		xml += "<FromUserName><![CDATA[2007000589]]></FromUserName>";
		xml += "<CreateTime>1418791339</CreateTime>";
		xml += "<MsgType><![CDATA[event]]></MsgType>";
		xml += "<Event><![CDATA[LOCATION]]></Event>";
		xml += "<Latitude>23.172237</Latitude>";
		xml += "<Longitude>113.427971</Longitude>";
		xml += "<Precision>40.000000</Precision>";
		xml += "<AgentID>1</AgentID>";
		xml += "</xml>";
		
		
//		xml = "<xml><ToUserName><![CDATA[wxbafe2b35c75e7daa]]></ToUserName>";
//		xml += "<FromUserName><![CDATA[2007000589]]></FromUserName>";
//		xml += "<CreateTime>1418791309</CreateTime>";
//		xml += "<MsgType><![CDATA[text]]></MsgType>";
//		xml += "<Content><![CDATA[测试]]></Content>";
//		xml += "<MsgId>4342223541257633801</MsgId>";
//		xml += "<AgentID>1</AgentID>";
//		xml += "</xml>";
		String handlerMsg = handlerMsg(xml);
		System.out.println("handlerMsg="+handlerMsg);
	}
	
	public static void main(String[] args) {
		String sToken = "nAU5v9EL";
		String sEncodingAESKey = "WsP9FetExvevxmzkJWl4ddc5BOSbDb6PN9nv3fnPkik";
		testReturnMsg(sToken, sEncodingAESKey);
		
	}
}