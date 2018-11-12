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

public class uploadPhotoAction extends BaseAction {

	Log logger = LogFactory.getLog(uploadPhotoAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("UTF-8");
		Date start = new Date();
		JSONObject json = new JSONObject();
		String href = request.getServletPath();
		String belongProject = "";
		String userName="";
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

			AppUserInfo vo = new AppUserInfo();
			if (type.equals("1")) {
				String head = object.getString("photo");
				String photoName = object.getString("photo_name");
				String nickName = object.getString("nick_name");
				String shuoMing = object.getString("shuo_ming");

				if (!"0".equals(head)) {
					String path = request.getSession(true).getServletContext()
							.getRealPath(Constant.USER_PHOTO_SAVE)
							+ userName;
					String fileName = Constant.getUniqueCode(userName) + ".png";
					/*File file = new File(path+"/"+fileName);
					if (file.exists()) {
						Constant.deleteFile(path);
					}*/
					Constant.createFileContent(path, fileName,
							Base64.decodeBase64(head));

//					String url = "http://" + getServerName() + ":";
					String url = "http://112.74.130.196:";
					int port = request.getServerPort();
					String downloadpath = request.getContextPath()
							+ Constant.USER_PHOTO_SAVE + "/"+ userName+"/"
							+ fileName;
					head = Constant.getDownloadPath(url, String.valueOf(port),
							downloadpath);
					vo.setHead(head);
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
				vo.setCondition("user_name='"+userName+"' and belong_project='"+belongProject+"'order by id desc");
				List<DataMap> listP=ServiceBean.getInstance().getAppUserInfoFacade().getUserPhotoInfo(vo);
				if(listP.size()>0){
					json.put("href",listP.get(0).get("photo_href")+"");
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
		insertVisit(href, belongProject, userName, 0, start, new Date(), json
				.toString().getBytes("utf-8").length);
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
