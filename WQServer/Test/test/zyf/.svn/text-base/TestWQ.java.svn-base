package test.zyf;

import com.sd.wq.aes.WXBizMsgCrypt;
import com.sd.wq.service.MessageUtil;


public class TestWQ {
	private static String sToken = "nAU5v9EL";
	private static String sEncodingAESKey = "WsP9FetExvevxmzkJWl4ddc5BOSbDb6PN9nv3fnPkik";
	public static void main(String[] args) throws Exception {
		String msg="<xml>";
		msg+="<ToUserName><![CDATA[wxbafe2b35c75e7daa]]></ToUserName>";
		msg+="<Encrypt><![CDATA[RPzzibPGLhPtqgg5vhFlIH4fQeMgjnYGr1I2HcWnsc0G/zXcVsj4P9tYLdJJVzJsSe3GqcqIHS7J6zVua+zxxOvU1UVqw3LrY/vE1LxNsaKPYNbAvpUW3gM8QaHHK0GPp1CZuMyefEQX/KBNb97v0lQctGYiE26bSvOa07W7UbJzoYmqUBed7r7VZRp6KA7OjqLDb/gAqY5Ar4zjPDLO4TMQzhpAXIunvb4B1TmrEPnnxNGirbOkZVMNqLf6qOfDfVghutiijL5asfjukb8UpgYjqvlSLMmAmrD6mNZq5/ekxbTeQSU9D1LJPfdImyGRo2oOh7W8XuxxksRYzvYQjsN9sugBSQ2Fx84UdUVjZkpbe3gSrEay6FdMy22Ka1IPwRi53a/zHncknRgyI9sxA2KS1in90P6ju5z09AUHfA2RtZKlKJGqeBosFAX+OeG6Rh4RbAWiwsPnIXDM4Ek4D3SVegjW7DEz4UG531lfaynZoVit5dyflbvqBDHMZjUW]]></Encrypt>";
		msg+="<AgentID><![CDATA[1]]></AgentID>";
		msg+="</xml>";
		String sReqMsgSig="3a0cd44f9b7df65793e92e3a1fe8cf54908ef4c8";
		String timestamp="1419234355";
		String nonce="163574098";
		
		
//		sReqMsgSig="9e0e679d58ca5059e0941bfeb55b280dcc1c9488";
//		timestamp="1419234375";
//		nonce="1734418751";
//		msg="<xml>";
//		msg+="<ToUserName><![CDATA[wxbafe2b35c75e7daa]]></ToUserName>";
//		msg+="<Encrypt><![CDATA[uNeT79J7CPXAIRK+v4tKHwtserPyamxyq47nMHCsJLVfc4aZpG2lltlryJ/0VZI7XvVxLOOyQA94Uo856Fx36AZvSpDO5I395Ee4pZssL+W1o868rAjX5UXrUGmJ/gGcLRMekrsWcPQFhoN2rHq/9KCExbjdRsFqJ9S2CNGYTiiAfyLYlGfZW7EOwwQtWnLvutcQWoAtLS0SuJBBLRctHBlulCUwF5xTKo0f6sJPMgrtOhirEQbbTS+i1v1viHfNhEK2WAuuTzT4sKijxV5UwmMJWKkSLe2co7A+kKZ/UoUwsOBava+YJsLOK8AbGhe1NxTifrxVVjIsi7wIV5tI1eelm8x36HRjxH6LloqSA2LHvx9JjLF61/0E576QZnX1/LtItipYjKLsCNPPJQ4mP3jc0FoeLLNVBtYa21wWhQZNjRH+lwd8vIqCeoQhcXkbera2dVm+lENlqTskgfExZ04IzRjE4x8Md7gJBuUL8C7sVXZNaLXmeKel99BdPobXLxR55Bhg4OOlQmYpk5PIePfkXOTeHyD6RbgDT6uauKU=]]></Encrypt>";
//		msg+="<AgentID><![CDATA[1]]></AgentID>";
//		msg+="</xml>";
				
		WXBizMsgCrypt wxcpt = MessageUtil.getWxcpt();
		String sMsg = wxcpt.DecryptMsg(sReqMsgSig, timestamp,nonce, msg);
		System.out.println(sMsg);
		
    }
}
