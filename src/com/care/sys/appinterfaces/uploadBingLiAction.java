package com.care.sys.appinterfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.care.common.config.ServiceBean;
import com.care.common.http.BaseAction;
import com.care.common.lang.Constant;
import com.care.sys.appuserinfo.domain.AppUserInfo;
import com.care.sys.appuserinfo.domain.logic.AppUserInfoFacade;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.log.LogFactory;

public class uploadBingLiAction extends BaseAction {

	Log logger = LogFactory.getLog(uploadBingLiAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("UTF-8");
		Date start = new Date();
		JSONObject json = new JSONObject();
		String href = request.getServletPath();
		String belongProject = "";
		String userName="";
		String reason="";
		String user_id="";
		try {
			ServletInputStream input = request.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			StringBuffer sb = new StringBuffer();
			String online = "";
			while ((online = reader.readLine()) != null) {
				sb.append(online);
			}
			JSONObject object = JSONObject.fromObject(sb.toString());
			// user_id = object.getString("user_id");
			String type = object.getString("type");// 1上传2查看
     		 userName = object.getString("user_name");
			belongProject = object.has("belong_project") ? object
					.getString("belong_project") : "1";
					user_id = object.has("user_id") ? object
							.getString("user_id") : "1";

			AppUserInfo vo = new AppUserInfo();
			if (type.equals("1")) {
				reason="上传病例";
				String head1 = object.has("photo1") ? object.getString("photo1") : "0";
				String head2 = object.has("photo2") ? object.getString("photo2") : "0";
				String head3 = object.has("photo3") ? object.getString("photo3") : "0";
				String head4 = object.has("photo4") ? object.getString("photo4") : "0";
				String head5 = object.has("photo5") ? object.getString("photo5") : "0";
				String head6 = object.has("photo6") ? object.getString("photo6") : "0";
				String photoName = object.getString("photo_name");
				String nickName = object.getString("nick_name");
				String shuoMing = object.getString("shuo_ming");

				if (!"0".equals(head1)) {
					String path = request.getSession(true).getServletContext()
							.getRealPath(Constant.USER_PHOTO_SAVE)
							+ userName;
					String fileName = Constant.getUniqueCode(userName) + ".png";
					/*File file = new File(path+"/"+fileName);
					if (file.exists()) {
						Constant.deleteFile(path);
					}*/
					Constant.createFileContent(path, fileName,
							Base64.decodeBase64(head1));

//					String url = "http://" + getServerName() + ":";
					String url = "http://112.74.130.196:";
					int port = request.getServerPort();
					String downloadpath = request.getContextPath()
							+ Constant.USER_PHOTO_SAVE + "/"+ userName+"/"
							+ fileName;
					head1 = Constant.getDownloadPath(url, String.valueOf(port),
							downloadpath);
					vo.setHead(head1);
				}
				if (!"0".equals(head2)) {
					String path = request.getSession(true).getServletContext()
							.getRealPath(Constant.USER_PHOTO_SAVE)
							+ userName;
					String fileName = Constant.getUniqueCode(userName) + ".png";
					/*File file = new File(path+"/"+fileName);
					if (file.exists()) {
						Constant.deleteFile(path);
					}*/
					Constant.createFileContent(path, fileName,
							Base64.decodeBase64(head2));

//					String url = "http://" + getServerName() + ":";
					String url = "http://112.74.130.196:";
					int port = request.getServerPort();
					String downloadpath = request.getContextPath()
							+ Constant.USER_PHOTO_SAVE + "/"+ userName+"/"
							+ fileName;
					head2 = Constant.getDownloadPath(url, String.valueOf(port),
							downloadpath);
					vo.setHead2(head2);
				}
				if (!"0".equals(head3)) {
					String path = request.getSession(true).getServletContext()
							.getRealPath(Constant.USER_PHOTO_SAVE)
							+ userName;
					String fileName = Constant.getUniqueCode(userName) + ".png";
					/*File file = new File(path+"/"+fileName);
					if (file.exists()) {
						Constant.deleteFile(path);
					}*/
					Constant.createFileContent(path, fileName,
							Base64.decodeBase64(head3));

//					String url = "http://" + getServerName() + ":";
					String url = "http://112.74.130.196:";
					int port = request.getServerPort();
					String downloadpath = request.getContextPath()
							+ Constant.USER_PHOTO_SAVE + "/"+ userName+"/"
							+ fileName;
					head3 = Constant.getDownloadPath(url, String.valueOf(port),
							downloadpath);
					vo.setHead3(head3);
				}
				if (!"0".equals(head4)) {
					String path = request.getSession(true).getServletContext()
							.getRealPath(Constant.USER_PHOTO_SAVE)
							+ userName;
					String fileName = Constant.getUniqueCode(userName) + ".png";
					/*File file = new File(path+"/"+fileName);
					if (file.exists()) {
						Constant.deleteFile(path);
					}*/
					Constant.createFileContent(path, fileName,
							Base64.decodeBase64(head4));

//					String url = "http://" + getServerName() + ":";
					String url = "http://112.74.130.196:";
					int port = request.getServerPort();
					String downloadpath = request.getContextPath()
							+ Constant.USER_PHOTO_SAVE + "/"+ userName+"/"
							+ fileName;
					head4 = Constant.getDownloadPath(url, String.valueOf(port),
							downloadpath);
					vo.setHead4(head4);
				}
				if (!"0".equals(head5)) {
					String path = request.getSession(true).getServletContext()
							.getRealPath(Constant.USER_PHOTO_SAVE)
							+ userName;
					String fileName = Constant.getUniqueCode(userName) + ".png";
					/*File file = new File(path+"/"+fileName);
					if (file.exists()) {
						Constant.deleteFile(path);
					}*/
					Constant.createFileContent(path, fileName,
							Base64.decodeBase64(head5));

//					String url = "http://" + getServerName() + ":";
					String url = "http://112.74.130.196:";
					int port = request.getServerPort();
					String downloadpath = request.getContextPath()
							+ Constant.USER_PHOTO_SAVE + "/"+ userName+"/"
							+ fileName;
					head5 = Constant.getDownloadPath(url, String.valueOf(port),
							downloadpath);
					vo.setHead(head5);
				}
				if (!"0".equals(head6)) {
					String path = request.getSession(true).getServletContext()
							.getRealPath(Constant.USER_PHOTO_SAVE)
							+ userName;
					String fileName = Constant.getUniqueCode(userName) + ".png";
					/*File file = new File(path+"/"+fileName);
					if (file.exists()) {
						Constant.deleteFile(path);
					}*/
					Constant.createFileContent(path, fileName,
							Base64.decodeBase64(head6));

//					String url = "http://" + getServerName() + ":";
					String url = "http://112.74.130.196:";
					int port = request.getServerPort();
					String downloadpath = request.getContextPath()
							+ Constant.USER_PHOTO_SAVE + "/"+ userName+"/"
							+ fileName;
					head6 = Constant.getDownloadPath(url, String.valueOf(port),
							downloadpath);
					vo.setHead6(head6);
				}
				userName = Constant.transCodingToUtf(userName); // ��Ҫת��
				vo.setNickName(nickName);
				vo.setPhotoName(photoName);
				vo.setBelongProject(belongProject);
				vo.setUserName(userName);
				vo.setCreateTime(new Date());
				vo.setShuoMing(shuoMing);
				int num = ServiceBean.getInstance().getAppUserInfoFacade()
						.insertUserPhotoInfo(vo);
				if (num > 0) {
					result = Constant.SUCCESS_CODE;
				} else {
					result = Constant.FAIL_CODE;
				}
			} else if (type.equals("2")) {
				reason="病例下载";
				vo.setCondition("user_name='"+userName+"' and belong_project='"+belongProject+"'order by id desc");
				List<DataMap> listP=ServiceBean.getInstance().getAppUserInfoFacade().getUserPhotoInfo(vo);
				if(listP.size()>0){
					json.put("photo1",listP.get(0).get("photo_href")+"");
					json.put("photo2",listP.get(0).get("head2")+"");
					json.put("photo3",listP.get(0).get("head3")+"");
					json.put("photo4",listP.get(0).get("head4")+"");
					json.put("photo5",listP.get(0).get("head5")+"");
					json.put("photo6",listP.get(0).get("head6")+"");
					result = Constant.SUCCESS_CODE;
				}else{
					result = Constant.FAIL_CODE;
				}
			}
			json.put("request", href);

		} catch (Exception e) {
			e.printStackTrace();
			StringBuffer sb = new StringBuffer();
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			Throwable cause = e.getCause();
			while (cause != null) {
				cause.printStackTrace(printWriter);
				cause = cause.getCause();
			}
			printWriter.close();
			String resultSb = writer.toString();
			sb.append(resultSb);

			logger.error(e);
			result = Constant.EXCEPTION_CODE;
			json.put(Constant.EXCEPTION, sb.toString());
		}
		json.put(Constant.RESULTCODE, result);
		insertVisitReason(href, belongProject, reason,user_id, 0, start, new Date(), json.toString().getBytes("utf-8").length);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());

		return null;
	}

	public String getServerName() throws Exception {
		String serverName = "";
		Properties pros = new Properties();
		pros.load(this.getClass().getClassLoader()
				.getResourceAsStream("server.properties"));
		serverName = pros.getProperty("servername");
		return serverName;
	}
}
