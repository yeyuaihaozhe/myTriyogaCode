package com.sd.wq.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/wq/phone")
public class PhoneLocationKeyController extends AbstractController{
	@RequestMapping("/locationkey")
	public String locationkey(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("进洞");
		String keyState = request.getParameter("keyState");
		System.out.println("keyState = "+keyState);
//		if("keyOpen".equals(keyState)){
//			response.setStatus(200);
//		}else{
//			response.setStatus(200);
//		}
		return "kaoqinzs/error";
	}
}