package com.sd.wq.job.oa;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.comba.util.CombaCommonUtil;
import com.comba.wq.dao.jdbc.JdbcDAO;
import com.comba.wq.util.SpringContextUtil;
import com.ibm.icu.util.Calendar;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.sd.wq.client.CombaWsClient;
import com.sd.wq.conf.SystemConfig;
import com.sd.wq.job.ScheduleUtil;

/**
 * 定时任务，定时读取数据库最新的id，然后该发给谁发出去
 * 
 * @author zhousheng
 * 
 */
public class OaJob1 {//implements Job {
//	private static Logger logger = Logger.getLogger(OaJob1.class);
//	private String group = "oa_group";
//	private String jobName = "OaJob1";
//	JdbcDAO oaJdbc = (JdbcDAO) SpringContextUtil.getBean("oaDb");
//	JdbcDAO wqJdbc = (JdbcDAO) SpringContextUtil.getBean("wqDb");
//	boolean flag = true;
//	@Override
//	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		try {
//			String sql = "select code from combawq_user";
//			List<Map<String, Object>> userList = wqJdbc.query(sql);
//			Map<String, String> userMap = new HashMap<String, String>();
//			for (Map<String, Object> map : userList) {
//				String code = CombaCommonUtil.getValueString(map, "code");
//				userMap.put(code, "1");
//			}
//			sql = "select * from comba_createpdfhtmlerror";
//			List<Map<String, Object>> errorIds = wqJdbc.query(sql);
//			for(Map<String, Object> errorId :errorIds){
//				flag = false;
//				int id = CombaCommonUtil.getValueInt(errorId, "docid");
//				sql = "delete from comba_createpdfhtmlerror where docid = ?";
//				wqJdbc.update(sql,id);
//				sendDoc(id,userMap);
//			}
//			flag = true;
//			logger.info("OaJob线程启动" + new Timestamp(System.currentTimeMillis()));
//			int maxId = getFromId(wqJdbc, oaJdbc);
//			sendDoc(maxId,userMap);
//		} catch (Exception e) {
//			logger.info("出现异常发送失败！！！！！");
//			logger.error(e.getMessage(), e);
//			// TODO 向数据库插入失败的信息
//		}
//	}
//	private void sendDoc(int maxId,Map<String, String> userMap){
//		
//		
//		// ----------------开始-----------------------------------------------------------------------------------------
//		// 获取标题和内容，注意查询的是id大于maxId的，所以没查询到内容下面的for循环也就不会执行
//		String sql = "select d.id,d.docsubject,c.doccontent,d.maincategory,d.subcategory from docdetail d left join docdetailcontent c on d.id=c.docid where d.id>? order by d.id";
//		List<Map<String, Object>> query = oaJdbc.query(sql, maxId);
//		// 获取微信里面的所有用户，并把工号作为key封装到map集合里面
//		// 遍历查询到的最新的doc文档
//		for (Map<String, Object> map : query) {
//			// 遍历一个文档就给maxId赋值为该文档id，最后的时候maxId会更新到数据库的表里面保存起来
//			maxId = CombaCommonUtil.getValueInt(map, "id");
//			// 判断是否在通知公告和公司新闻两个目录，如果不在这两个目录则不再继续改文档的操作
//			int maincategory = CombaCommonUtil.getValueInt(map,
//					"maincategory");
//			int subcategory = CombaCommonUtil.getValueInt(map,
//					"subcategory");
//
//			// maincategory
//			// + " subcategory=" + subcategory);
//			if (61 == maincategory) {
//				if (101 != subcategory && 102 != subcategory) {
//					continue;
//				}
//			} else {
//				continue;
//			}
//			// logger.info("send " + maxId);
//			// 获取标题
//			String title = CombaCommonUtil
//					.getValueString(map, "docsubject");
//			// 获取内容并过滤html标签和特殊字符
//			String filterContent = CombaCommonUtil.getValueString(map,
//					"doccontent");
//			filterContent = filterSC(filterContent);
//			// 如果内容大于30，则截取前面30个字
//			if (filterContent.length() > 30) {
//				filterContent = filterContent.substring(0, 30) + "..";
//			}
//			// 该Url是点击进入详细页面的链接
//			String url = "http://59.41.46.177:9981/weaver/rest/show/docdetail?id="
//					+ maxId;
//			// 固定为3，是发到企业号的哪个应用接收
//			String agentId = "3";
//			/**
//			 * TODO 要获取该公告要发给谁！！！！在docshare这个表里面有该文档分享的部门，分部，角色，人员的id
//			 * 然后根据这些id在公司员工表HRMRESOURCE里面查找这些部门，或分部或角色或人员的工号
//			 * ，其实微信那边做好了组织架构以后，只需要找出角色和人员的工号就行了，部门和分部可以直接传
//			 * （当更新分享了还要发给更新的共享的人。。。） 而且每次发完公告，都要在数据库表里面记录下来成功还是失败了
//			 */
//			// 发给哪个部门，先赋值为空
//			String toParty = "";
//			// 发给哪些用户，先赋值为空
//			StringBuilder toUser = new StringBuilder("");
//			// 找出该文档要发给的部门id，分部id，角色id，用户id，是否是所有人，群组id
//			String sharesql = "select departmentid,subcompanyid,userid,roleid,foralluser,orggroupid from docshare where docid=?";
//			List<Map<String, Object>> shares = oaJdbc
//					.query(sharesql, maxId);
//			// 遍历查找出来的要共享给谁的内容，给toParty和toUser赋值
//			for (Map<String, Object> share : shares) {
//				// 如果是发给所有人，则不必再遍历
//				if (CombaCommonUtil.getValueInt(share, "foralluser") != 0) {
//					// logger.info("所有人执行");
//					// 该部门对应于微信最顶级部门
//					toParty = "1";
//					toUser = new StringBuilder("");
//					break;
//				}
//				// logger.info("所有人后："+toUser.toString()+"    "+toParty);
//				// 部门
//				int deptid = CombaCommonUtil.getValueInt(share,
//						"departmentid");
//				if (deptid != 0) {
//					// logger.info("部门执行");
//					// System.out.println("部门号："+deptid);
//					// 员工的状态status不可以是离职的
//					sql = "select workcode from hrmresource where status in (0,1,2,3) and departmentid = ?";
//					List<Map<String, Object>> workcodes = oaJdbc.query(sql,
//							deptid);
//					for (Map<String, Object> workcodeMap : workcodes) {
//						String workcode = CombaCommonUtil.getValueString(
//								workcodeMap, "workcode");
//						String str = userMap.get(workcode);
//						// 要判断，这个工号是否已经遍历并存储到集合里面了，而且要判断这个工号是否在微信的工号范围里面
//						if ((!toUser.toString().contains(workcode + "|"))
//								&& null != str) {
//							toUser.append(workcode + "|");
//						}
//					}
//					continue;
//				}
//				// logger.info("部门后："+toUser.toString()+"    "+toParty);
//				// 分部
//				int subcid = CombaCommonUtil.getValueInt(share,
//						"subcompanyid");
//				if (subcid != 0) {
//					// logger.info("分部执行");
//					sql = "select workcode from hrmresource where status in (0,1,2,3) and subcompanyid1 = ?";
//					List<Map<String, Object>> workcodes = oaJdbc.query(sql,
//							subcid);
//					for (Map<String, Object> workcode : workcodes) {
//						if ((!toUser.toString().contains(
//								CombaCommonUtil.getValueString(workcode,
//										"workcode") + "|"))
//								&& null != userMap.get(CombaCommonUtil
//										.getValueString(workcode,
//												"workcode")))
//							toUser.append(CombaCommonUtil.getValueString(
//									workcode, "workcode") + "|");
//					}
//					continue;
//				}
//				// logger.info("分部后："+toUser.toString()+"    "+toParty);
//				// 用户
//				int userid = CombaCommonUtil.getValueInt(share, "userid");
//				// 如果为0则没有用户，如果为1则为系统管理员忽略
//				if (userid != 0 && userid != 1) {
//					sql = "select workcode from hrmresource where status in (0,1,2,3) and id = ?";
//					List<Map<String, Object>> workcode = oaJdbc.query(sql,
//							userid);
//					// TODO
//					// 因为现在是要直接取出来map集合，如果list集合为空的话则取不到，会空指针异常，前面的不会，因为是for循环遍历，其实这里也可以改成for循环遍历。。。。。
//					if (workcode.size() != 0) {
//						String strToUser = toUser.toString();
//						String workcodeStr = CombaCommonUtil
//								.getValueString(workcode.get(0), "workcode")
//								+ "|";
//						boolean f1 = !toUser.toString().contains(
//								CombaCommonUtil.getValueString(
//										workcode.get(0), "workcode")
//										+ "|");
//						boolean f2 = null != userMap
//								.get(CombaCommonUtil.getValueString(
//										workcode.get(0), "workcode"));
//						if ((!toUser.toString().contains(
//								CombaCommonUtil.getValueString(
//										workcode.get(0), "workcode")
//										+ "|"))
//								&& null != userMap.get(CombaCommonUtil
//										.getValueString(workcode.get(0),
//												"workcode"))) {
//							toUser.append(CombaCommonUtil.getValueString(
//									workcode.get(0), "workcode") + "|");
//						}
//						logger.info(toUser.toString());
//					}
//					continue;
//				}
//				// logger.info("用户后："+toUser.toString()+"    "+toParty);
//				// 角色
//				int roleid = CombaCommonUtil.getValueInt(share, "roleid");
//				if (roleid != 0) {
//					sql = "select workcode from hrmresource where status in (0,1,2,3) and id in (select resourceid from hrmrolemembers where roleid = ?)";
//					List<Map<String, Object>> workcodes = oaJdbc.query(sql,
//							roleid);
//					for (Map<String, Object> workcode : workcodes) {
//						if ((!toUser.toString().contains(
//								CombaCommonUtil.getValueString(workcode,
//										"workcode") + "|"))
//								&& null != userMap.get(CombaCommonUtil
//										.getValueString(workcode,
//												"workcode")))
//							toUser.append(CombaCommonUtil.getValueString(
//									workcode, "workcode") + "|");
//					}
//					continue;
//				}
//				// logger.info("角色后："+toUser.toString()+"    "+toParty);
//				// 群组
//				int groupid = CombaCommonUtil.getValueInt(share,
//						"orggroupid");
//				if (groupid != 0) {
//					sql = "select workcode from hrmresource where status in (0,1,2,3) and departmentid in (select content from hrmorggrouprelated where orggroupid = ?)";
//					List<Map<String, Object>> workcodes = oaJdbc.query(sql,
//							groupid);
//					System.out.println(workcodes.size());
//					for (Map<String, Object> workcode : workcodes) {
//						if ((!toUser.toString().contains(
//								CombaCommonUtil.getValueString(workcode,
//										"workcode") + "|"))
//								&& null != userMap.get(CombaCommonUtil
//										.getValueString(workcode,
//												"workcode")))
//							toUser.append(CombaCommonUtil.getValueString(
//									workcode, "workcode") + "|");
//					}
//					continue;
//				}
//				// logger.info("群组后："+toUser.toString()+"    "+toParty);
//			}// 遍历该文档的分享的for循环结束
//				// 该参数是微信里的标签，相当于oa里面的群组
//			String toTag = "";
//			// TODO 该参数不知道什么意思
//			int imageType = 0;
//			// 去除掉toUser字符串的最后一个|
//			if (toUser.length() != 0) {
//				toUser.deleteCharAt(toUser.length() - 1);
//
//			}
//			logger.info(toUser.toString() + " %%%%%%%%%%%%%%  " + toParty
//					+ "     " + maxId);
//			// 下面三个参数都不为空的时候才才给微信推送该文档
//			if (!(CombaCommonUtil.isEmpty(toUser)
//					&& CombaCommonUtil.isEmpty(toParty) && CombaCommonUtil
//						.isEmpty(toTag))) {
//
//				// TODO 如果有pdf文件，要把pdf转成图片存放在指定的位置
//				List<Map<String, Object>> imageFiles = getImageFiles(maxId);
//				for (Map<String, Object> fj : imageFiles) {
//
//					if (CombaCommonUtil.getValueString(fj, "imagefilename")
//							.toLowerCase().contains(".pdf")) {
//						logger.info("有pdf文件！！！！！");
//
//						// 防止下一个周期再次推送这个消息
//						if(flag){
//							updateMaxId(wqJdbc, maxId);
//						}
//						
//						try {
//							createPdfHtml(fj, maxId);
//						} catch (Exception e) {
//							e.printStackTrace();
//							//在数据库表comba_createpdfhtmlerror记录下来该docid
//							sql = "insert into comba_createpdfhtmlerror values(?)";
//							wqJdbc.update(sql,maxId);
//							logger.info("错误表中插入了id");
//							
//							sql = "select count from comba_count where docid=?";
//							List<Map<String, Object>> query2 = wqJdbc.query(sql, maxId);
//							if(query2.size()==0){
//								//在数据库表comba_count里面记录下来该docid的错误次数
//								sql = "insert into comba_count values(?,?)";
//								wqJdbc.update(sql,maxId,1);									
//							}else{
//								int count = CombaCommonUtil.getValueInt(query2.get(0), "count");
//								if(count>=3){
//									//TODO 给用户发送消息
//									CombaWsClient.sendMultyArticleMsg("0", "出错三次了",
//											maxId+"出错三次了","2014032106", "444",
//											toTag, url, imageType);
//								}else{
//									sql = "update comba_count set count = ? where docid = ?";
//									wqJdbc.update(sql,count+1,maxId);
//								}
//							}
//						}
//					}
//
//					// 推送
//					CombaWsClient.sendMultyArticleMsg(agentId, title,
//							filterContent, toUser.toString(), toParty,
//							toTag, url, imageType);
//					logger.info("已经推送");
//
//				}
//				// TODO 向数据库插入发送成功的信息
//			}// for循环结束
//				// 把最大id更新到数据库里面保存起来
//			if(flag){
//				updateMaxId(wqJdbc, maxId);				
//			}
//			logger.info("OaJob线程结束"
//					+ new Timestamp(System.currentTimeMillis()));
//			// -----------结束----------------------------------------------------------------------------------
//		}
//	
//	}
//	/**
//	 * 过滤特殊字符
//	 * 
//	 * @param filterContent
//	 *            传进来的未过滤的字符
//	 * @return 返回过滤后的字符
//	 */
//	private String filterSC(String filterContent) {
//		filterContent = filterContent.replaceAll("<[^>]+>", "");
//		filterContent = filterContent.replaceAll("\\s*|\t|\r|\n", "");
//		filterContent = filterContent.replaceAll("&nbsp;", "");
//		filterContent = filterContent.replaceAll("&emsp;", "");
//		filterContent = filterContent.replaceAll("&ensp;", "");
//		filterContent = filterContent.replaceAll("&lt;", "<");
//		filterContent = filterContent.replaceAll("&gt;", ">");
//		filterContent = filterContent.replaceAll("&amp;", "&");
//		filterContent = filterContent.replaceAll("&quot;", "\"");// ......双引号
//		filterContent = filterContent.replaceAll("&copy;", "@");
//		filterContent = filterContent.replaceAll("&reg;", "®");
//		filterContent = filterContent.replaceAll("&times;", "×");
//		filterContent = filterContent.replaceAll("&divide;", "÷");
//		filterContent = filterContent.replaceAll("&ldquo;", "“");
//		filterContent = filterContent.replaceAll("&rdquo;", "”");
//		return filterContent;
//	}
//
//	private void createPdfHtml(Map<String, Object> fj, int maxId) throws Exception {
//		
//		String filePath = "http://192.168.0.123/"
//				+ CombaCommonUtil.getValueString(fj, "filerealpath").substring(
//						21);
//		int imagefileid = CombaCommonUtil.getValueInt(fj, "imagefileid");
//
//		
//			pdfToImage(filePath, maxId, imagefileid);
//		
//
//		logger.info("已经将pdf转成了文件存放好了");
//
//		Date date = new Date();
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		String time = df.format(date);
//		System.out.println("当前日期是：" + time);
//
//		String htmlPath = SystemConfig.SYSTEM_PDF_IMAGER_FLODER + time + "\\"
//				+ maxId + "\\" + CombaCommonUtil.getValueInt(fj, "imagefileid")
//				+ "\\" + CombaCommonUtil.getValueString(fj, "imagefilename")
//				+ ".html";
//
//		
//			createHtml(htmlPath, filePath);
//		
//
//		logger.info("已经生成了html");
//	}
//
//	/**
//	 * 生成html
//	 * 
//	 * @param htmlPath
//	 *            html生成到哪里
//	 * @param filePath
//	 *            pdf文件在哪儿？用于计算pdf的页数，html里面好for循环几个img
//	 * @throws IOException
//	 */
//	private void createHtml(String htmlPath, String filePath) throws Exception {
//		// 生成html，里面的内容全是上面生成的图片
//		File imgHtml = new File(htmlPath);
//		if (!imgHtml.exists()) {
//			imgHtml.createNewFile();
//		}
//		FileWriter imgHtmlWriter = new FileWriter(imgHtml);
//		imgHtmlWriter
//				.write("<!DOCTYPE html><html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'><title>图片</title></head><body>");
//		PdfReader pdfReader1 = new PdfReader(filePath);
//		// 计算PDF页码数
//		int pageCount = pdfReader1.getNumberOfPages();
//		for (int i = 1; i <= pageCount; i++) {
//			imgHtmlWriter
//					.write("<img style='width: 100%;height: auto;' src='image/");
//			imgHtmlWriter.write(i + "");
//			imgHtmlWriter.write(".png'>");
//		}
//		imgHtmlWriter.write("</body></html>");
//		imgHtmlWriter.close();
//	}
//
//	private List<Map<String, Object>> getImageFiles(int id) {
//		JdbcDAO oaJdbc = (JdbcDAO) SpringContextUtil.getBean("oaDb");
//		String sql = "select f.imagefileid,f.filerealpath,f.filesize,d.imagefilename from imagefile f left join docimagefile d on f.imagefileid=d.imagefileid where d.docfiletype !=1 and d.docid=?";
//		List<Map<String, Object>> query = oaJdbc.query(sql, id);
//		return query;
//	}
//
//	/**
//	 * 将pdf转成图片
//	 * 
//	 * @param pdfUrl
//	 *            这个pdf文件的路径在哪儿？？
//	 * @param docId
//	 *            哪个公告的
//	 * @param imagefileid
//	 *            哪个pdf文件
//	 * @throws IOException
//	 * @throws DocumentException
//	 */
//	private void pdfToImage(String pdfUrl, int docId, int imagefileid)
//			throws Exception {
//		//模拟异常
//				if(1==1)
//				throw new IndexOutOfBoundsException();
//				
//		PdfReader pdfReader1 = new PdfReader(pdfUrl);
//		// 计算PDF页码数
//		int pageCount = pdfReader1.getNumberOfPages();
//
//		for (int i = 1; i <= pageCount; i++) {
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			PdfStamper pdfStamper = null;
//			PDDocument pdDocument = null;
//			PdfReader pdfReader = new PdfReader(pdfUrl);
//			pdfReader.selectPages(i + "");
//			pdfStamper = new PdfStamper(pdfReader, out);
//			pdfStamper.close();
//			// 利用PdfBox生成图像
//			pdDocument = PDDocument.load(new ByteArrayInputStream(out
//					.toByteArray()));
//
//			PDPage page = (PDPage) pdDocument.getDocumentCatalog()
//					.getAllPages().get(0);
//			BufferedImage image = page.convertToImage();
//
//			BufferedImage useimage = new BufferedImage(image.getWidth(),
//					image.getHeight(), BufferedImage.TYPE_INT_RGB);
//			useimage.getGraphics().drawImage(
//					image.getScaledInstance(image.getWidth(),
//							image.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
//
//			try {
//				Date date = new Date();
//				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//				String time = df.format(date);
//				File pdfimg = new File(SystemConfig.SYSTEM_PDF_IMAGER_FLODER
//						+ time + "\\" + docId + "\\" + imagefileid
//						+ "\\image\\" + i + ".png");
//				if (!pdfimg.exists()) {
//					pdfimg.mkdirs();
//				}
//				ImageIO.write(useimage, "PNG", pdfimg);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			/*
//			 * JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(res
//			 * .getOutputStream()); JPEGEncodeParam jep = JPEGCodec
//			 * .getDefaultJPEGEncodeParam(useimage); 压缩质量 jep.setQuality(1f,
//			 * true); encoder.encode(useimage, jep);
//			 */
//
//			pdDocument.close();
//		}
//
//	}
//
//	private void updateMaxId(JdbcDAO wqJdbc, int maxId) {
//		String sql = "update comba_msg_send set maxid=? where type_id=1";
//		wqJdbc.update(sql, maxId);
//	}
//
//	private int getFromId(JdbcDAO wqJdbc, JdbcDAO oaJdbc) {
//		int typeId = 1;
//		int fromId = 0;
//		String sql = "select t.type_id,t.maxid from comba_msg_send t where t.type_id=?";
//		List<Map<String, Object>> query = wqJdbc.query(sql, typeId);
//		if (query.size() == 1) {
//			Map<String, Object> map = query.get(0);
//			fromId = CombaCommonUtil.getValueInt(map, "maxid");
//		} else {
//			sql = "delete from comba_msg_send t where t.type_id=?";
//			wqJdbc.update(sql, typeId);
//
//			sql = "select max(id) as m from docdetail";
//			fromId = oaJdbc.queryForInt(sql);
//			sql = "insert into comba_msg_send(type_id,maxid) values(?,?)";
//			wqJdbc.update(sql, typeId, fromId);
//		}
//		return fromId;
//	}
//
//	public void start() {
//		try {
//			String begin = SystemConfig.SYSTEM_OAJOB1_BEGIN;
//			if (null == begin) {
//				begin = "2:00";
//			}
//			Timestamp startTime = getStartTime(begin);
//
//			int intervalInSeconds = 24 * 60 * 60;
//			try {
//				intervalInSeconds = Integer
//						.parseInt(SystemConfig.SYSTEM_OAJOB1_INTERVAL);
//			} catch (Exception e) {
//
//			}
//			// 启动早上同步的任务
//			ScheduleUtil.addJobRepeatForever(jobName, group, startTime,
//					intervalInSeconds, this.getClass());
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
//	}
//
//	private Timestamp getStartTime(String begin) {
//		String date = "2015-01-01";
//		Timestamp startTime = new Timestamp(System.currentTimeMillis());
//		try {
//			date = startTime.toString().split(" ")[0];
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
//		String str = date + " " + begin + ":00.0";
//		startTime = CombaCommonUtil.parseTimestamp(str);
//		logger.info(startTime);
//		return startTime;
//	}
//
//	public void shutdown() {
//		ScheduleUtil.shutdown(jobName, group);
//	}

}
