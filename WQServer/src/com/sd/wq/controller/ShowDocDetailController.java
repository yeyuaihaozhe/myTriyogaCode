package com.sd.wq.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.chanjar.weixin.cp.bean.WxCpJsapiSignature;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comba.util.CombaCommonUtil;
import com.sd.wq.client.CombaWsClient;

@Controller
@RequestMapping(value = "/show")
public class ShowDocDetailController extends AbstractController {
	/**
	 * 该方法根据传进来的docid，从数据库里面读取出来公告的标题，公告内容，以及生成下载链接
	 * 
	 * @param res
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/docdetail")
	public String docdetail(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		// 从参数里面获取文档id
		int id = Integer.parseInt(req.getParameter("id"));
		
		//获取微信JsApi Signature
		String url = "http://"+req.getLocalAddr()+":"+req.getLocalPort()+req.getRequestURI()+"?id="+String.valueOf(id);
		System.out.println("jsp address = "+url);
		WxCpJsapiSignature JsapiSignature = CombaWsClient.createJsapiSignature(url);
		if(null != JsapiSignature){
			System.out.println("JsApiSignature = "+CombaCommonUtil.jsonParseString(JsapiSignature));
			req.setAttribute("nonce", JsapiSignature.getNoncestr());
			req.setAttribute("signature", JsapiSignature.getSignature());
			req.setAttribute("timestamp", JsapiSignature.getTimestamp());
			req.setAttribute("appid", "wxbafe2b35c75e7daa");//企业号corpid
		}
		
		// 获取标题，内容，以及发布日期
		Map<String, Object> docDetail = getDocDetail(id);
		if (null != docDetail) {
			String title = CombaCommonUtil.getValueString(docDetail,
					"docsubject");
			req.setAttribute("title", title);

			String content = CombaCommonUtil.getValueString(docDetail,
					"doccontent");
			req.setAttribute("content", content);

			String createdate = CombaCommonUtil.getValueString(docDetail,
					"doccreatedate");
			req.setAttribute("createdate", createdate);
			Map<String, Object> initImageFilesInfo = initImageFilesInfo(id,createdate);
			req.setAttribute("filesInfo", initImageFilesInfo);
			return "doc/docdetail";
		}else{
			return "doc/error";
		}

	}

	private Map<String, Object> getDocDetail(int id) {
		String sql = "select dt.docsubject,dt.doccreatedate,dtc.doccontent from docdetail dt left join docdetailcontent dtc on dt.id=dtc.docid where dt.id="
				+ id;
		List<Map<String, Object>> query = getOaJDBC().query(sql);
		if (0 < query.size()) {
			return query.get(0);
		}
		return null;
	}

	private List<Map<String, Object>> getImageFiles(int id) {
		String sql = "select f.imagefileid,f.filerealpath,f.filesize,d.imagefilename from imagefile f left join docimagefile d on f.imagefileid=d.imagefileid where d.docfiletype !=1 and d.docid=?";
		List<Map<String, Object>> query = getOaJDBC().query(sql, id);
		return query;
	}
/**
 * 
 * @param id 是公告id
 * @return
 */
	private Map<String,Object> initImageFilesInfo(int id,String createdate) {
		// 获取附件名称和附件链接路径和附件的大小
		List<Map<String, Object>> imageFiles = getImageFiles(id);
		// 直接处理好生成链接路径和文件大小传过去，处理都在controller
		List<Map<String, Object>> normalList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> otherList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> link : imageFiles) {
			link.put("filesize",
					CombaCommonUtil.getValueInt(link, "filesize") / 1024);
			String fileName = CombaCommonUtil.getValueString(link,
					"imagefilename").toLowerCase();
			// 文件路径
			String filePath = "http://192.168.0.123/"
					+ CombaCommonUtil.getValueString(link, "filerealpath")
							.substring(21);
			// 文件大小
			String fileSize = CombaCommonUtil.getValueString(link, "filesize");
			if (Double.parseDouble(fileSize) > 1024) {
				link.put(
						"filesize",
						String.format("%.2f",
								Double.parseDouble(fileSize) / 1024) + "M");
			} else {
				link.put("filesize",
						String.format("%.2f", Double.parseDouble(fileSize))
								+ "KB");
			}
			int type = 1;
			// 文件链接
			if (fileName.contains(".png") || fileName.contains(".jpg")
					|| fileName.contains("jpeg")) {
				link.put("linkpath", "/weaver/openimage.jsp?path="
						+ filePath + "&filename=" + fileName);
			} else if (fileName.contains(".txt")) {
				link.put("linkpath",
						"http://59.41.46.177:9981/weaver/opendirect?openpath="
								+ filePath);
			} else if (fileName.contains(".pdf")) {
//				link.put("linkpath",
//						"http://59.41.46.177:9981/weaver/rest/page/pagecount?filepath="
//								+ filePath + "&filename=" + fileName);
				int imagefileid = CombaCommonUtil.getValueInt(link, "imagefileid");
				/*Program Files (x86)/Apache Software Foundation/Apache2.2/htdocs*/
				link.put("linkpath","/oamsg/"+createdate+"/"+id+"/"+imagefileid+"/"+fileName+".html");
			} else {
				type = 2;
				link.put("linkpath",
						"http://59.41.46.177:9981/weaver/rest/d/download?filepath="
								+ filePath + "&filename=" + fileName);
			}
			if (1 == type) {
				normalList.add(link);
			} else {
				otherList.add(link);
			}
		}
		Map<String,Object> returnMap=new HashMap<String,Object>();
		returnMap.put("normalList", normalList);
		returnMap.put("otherList", otherList);
		returnMap.put("otherSize", otherList.size());
		return returnMap;
	}
}
