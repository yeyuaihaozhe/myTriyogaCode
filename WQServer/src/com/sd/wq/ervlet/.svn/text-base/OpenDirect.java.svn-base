package com.sd.wq.ervlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ģ�������õ�servlet
 */
public class OpenDirect extends HttpServlet {

	/**
	 * Default constructor.
	 */
	public OpenDirect() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		OutputStream os = null;
		String openpath = req.getParameter("openpath");
		try {
			os = res.getOutputStream();
			URL url = new URL(openpath);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setDoInput(true);// �����Ƿ�Ҫ�� URL ���Ӷ�ȡ���,Ĭ��Ϊtrue
			uc.connect();
			InputStream iputstream = uc.getInputStream();
			byte[] buffer = new byte[4 * 1024];
			int byteRead = -1;
			while ((byteRead = (iputstream.read(buffer))) != -1) {
				os.write(buffer, 0, byteRead);
			}
			os.flush();
			iputstream.close();
			os.close();
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

