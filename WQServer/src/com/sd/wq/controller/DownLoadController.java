package com.sd.wq.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comba.util.CombaCommonUtil;
import com.sd.wq.client.CombaWsClient;

@Controller
@RequestMapping(value = "/d")
public class DownLoadController extends AbstractController {

	/**
	 * 下载附件用
	 * 
	 * @param res
	 * @param req
	 * @throws IOException
	 */
	@RequestMapping("download")
	public void download(HttpServletResponse res, HttpServletRequest req)
			throws IOException {
		OutputStream os = null;
		String fUrl = "http://192.168.2.81/filesystem/201501/S/1285786945.zip?fileName=1285786945.zip";
		System.out.println(req.getParameter("filepath"));
		String uri = req.getParameter("filepath").substring(21);
		String fileName = new String(req.getParameter("filename").getBytes(
				"iso-8859-1"), "utf-8");
		fUrl = "http://192.168.0.123/" + uri + "?fileName=" + fileName;
		System.out.println("fUrl:" + fUrl);
		try {
			res.reset();
			res.setContentType("application/octet-stream; charset=utf-8");
			res.setHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(fileName, "UTF-8"));
			os = res.getOutputStream();
			URL url = new URL(fUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setDoInput(true);// 设置是否要从 URL 连接读取数据,默认为true
			uc.connect();
			
			InputStream iputstream = uc.getInputStream();
			System.out.println("file size is:" + uc.getContentLength());// 打印文件长度
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
	 * 下载图片用
	 * @param res
	 * @param req
	 * @throws IOException
	 */
	@RequestMapping("downloadByFileId")
	public void downloadByFileId(HttpServletResponse res, HttpServletRequest req)
			throws IOException {
		System.out.println(22222222);
		//获取参数
		int fileid = Integer.parseInt(req.getParameter("fileid"));			
		//通过参数（imagefile表的id）从数据库里读到该图片的文件大小和文件路径
		String sql = "select filesize,filerealpath from imagefile where imagefileid = ?";
		List<Map<String, Object>> filepath = getOaJDBC()
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
	 * 列表，自己用，发信息给微信，主要。内容包括公告标题，公告内容，和访问该公告详细信息的链接 该链接调用下面的docdetail方法
	 * 
	 * @param res
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/doclist")
	public String doclist(HttpServletResponse res, HttpServletRequest req)
			throws IOException {
		int id = 0;
		// 访问数据库获取表里面内容
		for (int i = 0; i < 1; i++) {

			id = Integer.parseInt(req.getParameter("id"));

			CombaWsClient ws = new CombaWsClient();

			// 获取标题
			String sql = "select d.docsubject,c.doccontent from docdetail d left join docdetailcontent c on d.id=c.docid where d.id="
					+ id;
			List<Map<String, Object>> query = getOaJDBC().query(sql);
			req.setAttribute("title" + i, query);
			Map<String, Object> map = query.get(0);
			String title = CombaCommonUtil.getValueString(map, "docsubject");
			String filterContent = CombaCommonUtil.getValueString(map,
					"doccontent");
			req.setAttribute("content" + i, query);
			filterContent = filterContent.replaceAll("<[^>]+>", "");
			filterContent = filterContent.replaceAll("\\s*|\t|\r|\n", "");
			filterContent = filterContent.replaceAll("&nbsp;", "");
			filterContent = filterContent.replaceAll("&emsp;", "");
			filterContent = filterContent.replaceAll("&ensp;", "");
			filterContent = filterContent.replaceAll("&lt;", "<");
			filterContent = filterContent.replaceAll("&gt;", ">");
			filterContent = filterContent.replaceAll("&amp;", "&");
			filterContent = filterContent.replaceAll("&quot;", "\"");//......双引号
			filterContent = filterContent.replaceAll("&copy;", "@");
			filterContent = filterContent.replaceAll("&reg;", "®");
			filterContent = filterContent.replaceAll("&times;", "×");
			filterContent = filterContent.replaceAll("&lt;", "÷");
			System.out.println(filterContent);

			req.setAttribute("link", "fdfd");
			// 这个是访问真正部署上去的那个服务器的我下面写的docdetail方法
			String url = "http://59.41.46.177:9981/weaver/rest/show/docdetail?id="
					+ id;
			String agentId = "3";
			String toParty = "";
			StringBuilder toUser = new StringBuilder("");
			String sharesql = "select departmentid,subcompanyid,userid,roleid,foralluser,orggroupid from docshare where docid=?";
			List<Map<String, Object>> shares = getOaJDBC()
					.query(sharesql, id);
			for (Map<String, Object> share : shares) {
				//所有人
				if (CombaCommonUtil.getValueInt(share, "foralluser") != 0) {
					// 该部门对应于微信最顶级部门
					toParty = "1";
					toUser = new StringBuilder("");
					break;
				}
				//部门
				int deptid = CombaCommonUtil.getValueInt(share, "departmentid");
				if ( deptid!= 0) {
					sql = "select workcode from hrmresource where status in (0,1,2,3) and departmentid = ?";
					List<Map<String, Object>> workcodes = getOaJDBC()
							.query(sql, deptid);
					for(Map<String, Object> workcode : workcodes){
						if(!toUser.toString().contains(CombaCommonUtil.getValueString(workcode, "workcode")+"|"))
						toUser.append(CombaCommonUtil.getValueString(workcode, "workcode")+"|");
					}
					System.out.println(toUser.toString()+"   "+toParty);
					continue;
				}
				//分部
				int subcid = CombaCommonUtil.getValueInt(share, "subcompanyid");
				if ( subcid!= 0) {
					sql = "select workcode from hrmresource where status in (0,1,2,3) and subcompanyid1 = ?";
					List<Map<String, Object>> workcodes = getOaJDBC()
							.query(sql, subcid);
					for(Map<String, Object> workcode : workcodes){
						if(!toUser.toString().contains(CombaCommonUtil.getValueString(workcode, "workcode")+"|"))
						toUser.append(CombaCommonUtil.getValueString(workcode, "workcode")+"|");
					}
					System.out.println(toUser.toString()+"   "+toParty);
					continue;
				}
				//用户
				int userid = CombaCommonUtil.getValueInt(share, "userid");
				if ( userid != 0&&userid!=1) {
					sql = "select workcode from hrmresource where status in (0,1,2,3) and id = ?";
					List<Map<String, Object>> workcode = getOaJDBC()
							.query(sql, userid);
					if(!toUser.toString().contains(CombaCommonUtil.getValueString(workcode.get(0), "workcode")+"|"))
					toUser.append(CombaCommonUtil.getValueString(workcode.get(0), "workcode")+"|");
					System.out.println(toUser.toString()+"   "+toParty);
					continue;
				}
				//角色
				int roleid = CombaCommonUtil.getValueInt(share, "roleid");
				if (roleid != 0) {
					sql = "select workcode from hrmresource where status in (0,1,2,3) and id in (select resourceid from hrmrolemembers where roleid = ?)";
					List<Map<String, Object>> workcodes = getOaJDBC()
							.query(sql, roleid);
					for(Map<String, Object> workcode : workcodes){
						if(!toUser.toString().contains(CombaCommonUtil.getValueString(workcode, "workcode")+"|"))
						toUser.append(CombaCommonUtil.getValueString(workcode, "workcode")+"|");
					}
					System.out.println(toUser.toString()+"   "+toParty);
					continue;
				}
				//群组
				int groupid = CombaCommonUtil.getValueInt(share, "orggroupid");
				if (groupid != 0) {
					sql = "select workcode from hrmresource where status in (0,1,2,3) and departmentid in (select content from hrmorggrouprelated where orggroupid = ?)";
					List<Map<String, Object>> workcodes = getOaJDBC()
							.query(sql, groupid);
					System.out.println(workcodes.size());
					for(Map<String, Object> workcode : workcodes){
						if(!toUser.toString().contains(CombaCommonUtil.getValueString(workcode, "workcode")+"|"))
						toUser.append(CombaCommonUtil.getValueString(workcode, "workcode")+"|");
					}
					System.out.println(toUser.toString()+"   "+toParty);
					continue;
				}
			}
			
			String toTag = "";
			int imageType = 0;
			if(toUser.length()!=0)
			toUser.deleteCharAt(toUser.length()-1);
			System.out.println(toUser.toString()+"   "+toParty);
			ws.sendMultyArticleMsg(agentId, title, filterContent, toUser.toString(),
					toParty, toTag, url, imageType);
			System.out.println("调用接口完成");
		}
		return "doc/doclist";
	}

	/**
	 * 该方法根据传进来的docid，从数据库里面读取出来公告的标题，公告内容，以及生成下载链接
	 * 
	 * @param res
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/docdetail")
	public String docdetail(HttpServletResponse res, HttpServletRequest req)
			throws IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		// 访问数据库获取表里面内容

		// 获取标题
		String sql = "select docsubject from docdetail where id=" + id;
		List<Map<String, Object>> query = getOaJDBC().query(sql);
		String filterTitle = (String) query.get(0).get("docsubject");
		String reg = "<[^>]+>";
		filterTitle = filterTitle.replaceAll(reg, "");
		filterTitle = filterTitle.trim();
		System.out.println(filterTitle);
		req.setAttribute("title", filterTitle);

		// 获取内容
		sql = "select doccontent from docdetailcontent where docid=" + id;
		query = getOaJDBC().query(sql);
		// 得到内容字符串
		String filterContent = (String) query.get(0).get("doccontent");
		filterContent = filterContent.replaceAll(reg, "");
		filterContent = filterContent.replaceAll("\\s*|\\t|\\r|\\n", "");
		filterContent = filterContent.replaceAll("&nbsp;", "");
		System.out.println(filterContent);
		req.setAttribute("content", filterContent);

		// 获取附件链接
		sql = "select filerealpath from imagefile where imagefileid = (select imagefileid from docimagefile where docid="
				+ id + ")";
		query = getOaJDBC().query(sql);
		String linkpath = (String) query.get(0).get("filerealpath");
		System.out.println(linkpath);
		req.setAttribute("link", linkpath);

		return "doc/docdetail";
	}

	/**
	 * 文件下载方法p
	 * 
	 * @param fUrl
	 *            源文件地址 如：?fileName=demo.txt
	 * @param sUrl
	 *            保存地址 如：D://
	 * @return
	 */
	public boolean DownFile(String fUrl, String sUrl) {
		String[] urlname = fUrl.split("=");
		int len = urlname.length - 1;
		String uname = urlname[len];// 获取文件名
		try {
			File file = new File(sUrl + uname);// 创建新文件
			if (file != null && !file.exists()) {
				// TODO 忘记了。。。。
				file.createNewFile();
			}
			OutputStream oputstream = new FileOutputStream(file);
			URL url = new URL(fUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setDoInput(true);// 设置是否要从 URL 连接读取数据,默认为true
			uc.connect();
			InputStream iputstream = uc.getInputStream();
			System.out.println("file size is:" + uc.getContentLength());// 打印文件长度
			byte[] buffer = new byte[4 * 1024];
			int byteRead = -1;
			while ((byteRead = (iputstream.read(buffer))) != -1) {
				oputstream.write(buffer, 0, byteRead);
			}
			oputstream.flush();
			iputstream.close();
			oputstream.close();
			System.out.println("源文件：" + fUrl);
			System.out.println("下载到：" + sUrl + uname);
			return true;

		} catch (Exception e) {
			System.out.println("下载失败！");
			e.printStackTrace();
			return false;
		}
	}

	// 下载并输出内容。
	public static String DownAndReadFile(String filePath) {
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		File savePath = new File("D://" + date);// 创建新文件
		if (!savePath.exists()) {
			savePath.mkdir();
		}
		String[] urlname = filePath.split("=");
		int len = urlname.length - 1;
		String uname = urlname[len];// 获取文件名
		try {
			File file = new File(savePath + "//" + uname);// 创建新文件
			if (file != null && !file.exists()) {
				file.createNewFile();
			}
			OutputStream oputstream = new FileOutputStream(file);
			URL url = new URL(filePath);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setDoInput(true);// 设置是否要从 URL 连接读取数据,默认为true
			uc.connect();
			InputStream iputstream = uc.getInputStream();
			System.out.println("file size is:" + uc.getContentLength());// 打印文件长度
			byte[] buffer = new byte[4 * 1024];
			int byteRead = -1;
			while ((byteRead = (iputstream.read(buffer))) != -1) {
				oputstream.write(buffer, 0, byteRead);
			}
			oputstream.flush();
			iputstream.close();
			oputstream.close();
			// 读取文件
			StringBuffer strb = new StringBuffer();

			FileInputStream fs = new FileInputStream(new File(savePath + "//"
					+ uname));

			InputStreamReader isr = new InputStreamReader(fs, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String data = "";
			while ((data = br.readLine()) != null) {
				strb.append(data + "\n");
			}
			br.close();
			fs.close();
			isr.close();
			System.out.println(strb.toString());
			return strb.toString();
		} catch (Exception e) {
			System.out.println("读取失败！");
			e.printStackTrace();
		}
		return null;
	}
}