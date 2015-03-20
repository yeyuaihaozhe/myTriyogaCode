package com.sd.wq.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/triyoga")
public class TriyogaController extends AbstractController{
	@RequestMapping("/index")
	public String locationkey(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("Come come in !");
		String keyState = request.getParameter("state");
		System.out.println("State = "+keyState);
		return "triyoga/index";
	}
}