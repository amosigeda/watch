package com.care.sys.appinterfaces;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.care.common.config.ServiceBean;
import com.care.common.http.BaseAction;
import com.care.common.lang.Constant;
import com.care.sys.checkinfo.domain.CheckInfo;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.log.LogFactory;

public class updateAppNewAction extends BaseAction {

	Log logger = LogFactory.getLog(updateAppNewAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("UTF-8");
		String href = request.getServletPath();
		Date start = new Date();
		JSONObject json = new JSONObject();
		String belongProject="";
		String user_idd="";
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
			// String user_id =
			// object.has("user_id")?object.getString("user_id"):"-1";
			String version_code = object.getString("version_code");// 版本号
			 belongProject = object.getString("belong_project");// 项目编号
			// belongProject = AESPlus.decrypt(Constant.secretKey,
			// belongProject);
			String packages = object.has("pa") ? object.getString("pa") : "-1";// 包名
			String userId = object.getString("user_id");
			user_idd=userId;
			CheckInfo vo = new CheckInfo();
			CheckInfo voW = new CheckInfo();
			if (!version_code.equals("") && !belongProject.equals("")
					&& !packages.equals("")) {
				vo.setCondition("belong_project ='" + belongProject
						+ "' and status = '1' and package_name ='" + packages
						+ "' and version_type='0' order by id desc limit 0,1");// 先查内部版本
				List<DataMap> List = ServiceBean.getInstance().getCheckInfoFacade().getCheckInfo(vo);
				voW.setCondition("belong_project ='" + belongProject
						+ "' and status = '1' and package_name ='" + packages
						+ "' and version_type='1' order by id desc limit 0,1");// 查外部版本
				List<DataMap> ListW = ServiceBean.getInstance()
						.getCheckInfoFacade().getCheckInfo(voW);
				if (List.size() > 0) {// 内部

					String quanXian = List.get(0).get("quan_xian") + "";
					String[] userArr = quanXian.split(",");
					int length = userArr.length;
					boolean isInner =false;
					for (int i = 0; i < length; i++) {
						quanXian = userArr[i];
					
						if (quanXian.equals(userId)) {
							isInner = true;
							String name = List.get(0).get("version_name") + "";
							String path = List.get(0).get("download_path") + "";
							String cap = List.get(0).get("function_cap") + "";
							String cape = List.get(0).get("function_cape") + "";
							String type = List.get(0).get("up_type") + "";
							String versionCode = List.get(0)
									.get("version_code") + "";
							int banben = Integer.valueOf(versionCode);
							if (banben > Integer.valueOf(version_code)) {
								json.put("apk_version_code", versionCode);
								json.put("apk_version_name", name);
								json.put("download_url", path);
								json.put("apk_function_cap", cap);
								json.put("up_type", type);
								json.put("apk_function_cape", cape);
								result = Constant.SUCCESS_CODE;
							} else {
								result = Constant.FAIL_CODE;
							}
							break;
						} }
					
					
					if (!isInner) {

						String versionCode = ListW.get(0).get("version_code")
								+ "";
						String name = ListW.get(0).get("version_name") + "";
						String path = ListW.get(0).get("download_path") + "";
						String cap = ListW.get(0).get("function_cap") + "";
						String cape = ListW.get(0).get("function_cape") + "";
						String type = ListW.get(0).get("up_type") + "";
						int banben = Integer.valueOf(versionCode);
						if (banben > Integer.valueOf(version_code)) {
							json.put("apk_version_code", versionCode);
							json.put("apk_version_name", name);
							json.put("download_url", path);
							json.put("apk_function_cap", cap);
							json.put("up_type", type);
							json.put("apk_function_cape", cape);
							result = Constant.SUCCESS_CODE;
						} else {
							result = Constant.FAIL_CODE;
						}

					} /*else {
						result = Constant.FAIL_CODE;
					}*/
					
					/*else {
							if (ListW.size() > 0) {

								String versionCode = ListW.get(0).get(
										"version_code")
										+ "";
								String name = ListW.get(0).get("version_name")
										+ "";
								String path = ListW.get(0).get("download_path")
										+ "";
								String cap = ListW.get(0).get("function_cap")
										+ "";
								String cape = ListW.get(0).get("function_cape")
										+ "";
								String type = ListW.get(0).get("up_type") + "";
								int banben = Integer.valueOf(versionCode);
								if (banben > Integer.valueOf(version_code)) {
									json.put("apk_version_code", versionCode);
									json.put("apk_version_name", name);
									json.put("download_url", path);
									json.put("apk_function_cap", cap);
									json.put("up_type", type);
									json.put("apk_function_cape", cape);
									result = Constant.SUCCESS_CODE;
								} else {
									result = Constant.FAIL_CODE;
								}

							} else {
								result = Constant.FAIL_CODE;
							}
						}*/
					

				} 

				/*
				 * voW.setCondition("belong_project ='" + belongProject +
				 * "' and status = '1' and package_name ='" + packages +
				 * "' and version_type='1' order by id desc limit 0,1");// 查外部版本
				 * List<DataMap> ListW = ServiceBean.getInstance()
				 * .getCheckInfoFacade().getCheckInfo(voW); if (ListW.size() >
				 * 0) {
				 * 
				 * String versionCode = ListW.get(0).get("version_code") + "";
				 * String name = ListW.get(0).get("version_name") + ""; String
				 * path = ListW.get(0).get("download_path") + ""; String cap =
				 * ListW.get(0).get("function_cap") + ""; String cape =
				 * ListW.get(0).get("function_cape") + ""; String type =
				 * ListW.get(0).get("up_type") + ""; int banben =
				 * Integer.valueOf(versionCode); if (banben >
				 * Integer.valueOf(version_code)) { json.put("apk_version_code",
				 * versionCode); json.put("apk_version_name", name);
				 * json.put("download_url", path); json.put("apk_function_cap",
				 * cap); json.put("up_type", type);
				 * json.put("apk_function_cape", cape); result =
				 * Constant.SUCCESS_CODE; } else { result = Constant.FAIL_CODE;
				 * }
				 * 
				 * } else { result = Constant.FAIL_CODE; }
				 */
			} else {
				result = Constant.FAIL_CODE;
			}

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
		json.put("request", href);
		json.put(Constant.RESULTCODE, result);
		insertVisitReason(href, belongProject, "APP版本升级升级", user_idd, 0, start, new Date(), json.toString().getBytes("utf-8").length);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());

		return null;
	}
}
