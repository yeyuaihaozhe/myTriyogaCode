package com.sd.wq.ervlet;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.comba.util.CombaCommonUtil;
import com.comba.wq.dao.jdbc.JdbcDAO;
import com.comba.wq.util.SpringContextUtil;

/**
 * ģ�������õ�servlet
 */
public class FileDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public FileDownload() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		System.out.println(8888777);
		JdbcDAO oaJDBC = (JdbcDAO)SpringContextUtil.getBean("oaDb");
		System.out.println(22222222);
		//获取参数
		int fileid = Integer.parseInt(req.getParameter("fileid"));			
		//通过参数（imagefile表的id）从数据库里读到该图片的文件大小和文件路径
		String sql = "select filesize,filerealpath from imagefile where imagefileid = ?";
		List<Map<String, Object>> filepath = oaJDBC
				.query(sql, fileid);
		String filesize = CombaCommonUtil.getValueString(filepath.get(0), "filesize");
		System.out.println("文件大小："+filesize);
		String fUrl = CombaCommonUtil.getValueString(filepath.get(0), "filerealpath");
		fUrl = "http://192.168.0.123/" + fUrl.substring(21);
//		fUrl = "http://192.168.0.197:9981/ImgName-1.png";
		OutputStream os = null;
		try {
			res.reset();
			res.setContentType("application/octet-stream; charset=utf-8");
			//TODO 文件名应该是图片文件名
			res.setHeader("Content-Disposition", "attachment;");
			os = res.getOutputStream();
			URL url = new URL(fUrl);
			
			//下面的是压缩操作，可以根据文件大小进行压缩，这里获取文件大小比较方便
			InputStream is = url.openStream();
			Image src = ImageIO.read(is);
			int realw = src.getWidth(null);
			int realh = src.getHeight(null);
			int width = 500;
			int height = width*realh/realw;
			System.out.println(src.getWidth(null)+"    "+src.getHeight(null));
			
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			 tag.getGraphics().drawImage(src.getScaledInstance(width, height,  Image.SCALE_SMOOTH), 0,0,null);
			 
			 try {  
		            ImageIO.write(tag, "PNG", os);  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }

//             newimage=new FileOutputStream(newFile); //输出到文件流
            /* JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
              // 压缩质量 
             jep.setQuality(1f, true);
             encoder.encode(tag, jep);*/
			
			
			
//			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
//			uc.setDoInput(true);// 设置是否要从 URL 连接读取数据,默认为true
//			uc.connect();
//
//			InputStream iputstream = uc.getInputStream();
//			byte[] buffer = new byte[4 * 1024];
//			int byteRead = -1;
//			while ((byteRead = (iputstream.read(buffer))) != -1) {
//				os.write(buffer, 0, byteRead);
//			}
			os.flush();
//			iputstream.close();
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
