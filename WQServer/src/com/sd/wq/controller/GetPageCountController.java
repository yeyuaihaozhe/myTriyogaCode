package com.sd.wq.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lowagie.text.pdf.PdfReader;

@Controller
@RequestMapping(value = "/page")
public class GetPageCountController extends AbstractController {
	/**
	 * 该方法根据传进来的docid，从数据库里面读取出来公告的标题，公告内容，以及生成下载链接
	 * @param res
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/pagecount")  
	public String docdetail(HttpServletResponse res,HttpServletRequest req) throws IOException { 
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");
		String path = req.getParameter("filepath");
		
		//读入PDF
		 PdfReader pdfReader = new PdfReader(path);		 
		//计算PDF页码数
	     int pageCount = pdfReader.getNumberOfPages();	
	     
	     req.setAttribute("path",path);
	     req.setAttribute("pagecount", pageCount);
		return "doc/pdflist";
	}
}
