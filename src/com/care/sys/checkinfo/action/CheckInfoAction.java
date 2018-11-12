package com.care.sys.checkinfo.action;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.care.app.LoginUser;
import com.care.common.config.Config;
import com.care.common.config.ServiceBean;
import com.care.common.http.BaseAction;
import com.care.common.lang.CommUtils;
import com.care.common.lang.Constant;
import com.care.sys.appuserinfo.domain.AppUserInfo;
import com.care.sys.checkinfo.domain.CheckInfo;
import com.care.sys.checkinfo.domain.logic.CheckInfoFacade;
import com.care.sys.checkinfo.form.CheckInfoForm;
import com.care.sys.projectinfo.domain.ProjectInfo;
import com.godoing.rose.http.common.HttpTools;
import com.godoing.rose.http.common.PagePys;
import com.godoing.rose.http.common.Result;
import com.godoing.rose.lang.DataList;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.lang.SystemException;
import com.godoing.rose.log.LogFactory;
import com.sinaapp.msdxblog.apkUtil.utils.ApkUtil;

public class CheckInfoAction extends BaseAction {
	Log logger = LogFactory.getLog(CheckInfoAction.class);

	public ActionForward queryCheckInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Date start = new Date();
		String href = request.getServletPath();
		Result result = new Result();
		PagePys pys = new PagePys();
		DataList list = null;
		StringBuffer sb = new StringBuffer();
		CheckInfoFacade info = ServiceBean.getInstance().getCheckInfoFacade();
		try {
			CheckInfoForm form = (CheckInfoForm) actionForm;
			CheckInfo vo = new CheckInfo();
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String versionCode = request.getParameter("versionCode");
			String belongProject = request.getParameter("belongProject");
			String upType = request.getParameter("upType");
			String versionType = request.getParameter("versionType");
			String status = request.getParameter("status");

			LoginUser loginUser = (LoginUser) request.getSession()
					.getAttribute(Config.SystemConfig.LOGINUSER);
			if (loginUser == null) {
				return null;
			}
			String companyInfoId = loginUser.getCompanyId();
			String projectInfoId = loginUser.getProjectId();

			form.setOrderBy("c.upload_time");
			form.setSort("1");

			if (!"0".equals(projectInfoId)) { // projectInfoId.equals("0")
				sb.append("c.belong_project in(" + projectInfoId + ")");
			} else {
				if (!"0".equals(companyInfoId)) { // companyInfoId
					ProjectInfo pro = new ProjectInfo();
					pro.setCondition("company_id in(" + companyInfoId + ")");
					List<DataMap> proList = ServiceBean.getInstance()
							.getProjectInfoFacade().getProjectInfo(pro);
					if (proList.size() > 0) {
						int num = proList.size();
						String str = "";
						for (int i = 0; i < num; i++) {
							Integer id = (Integer) proList.get(i).getAt("id");
							str += String.valueOf(id);
							if (i != num - 1) {
								str += ",";
							}
						}
						sb.append("c.belong_project in(" + str + ")");
					}
				}
			}

			if (startTime != null && !"".equals(startTime)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("substring(upload_time,1,10) >= '" + startTime + "'");
			}
			if (endTime != null && !"".equals(endTime)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("substring(upload_time,1,10) <= '" + endTime + "'");
			}
			if (versionCode != null && !"".equals(versionCode)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("c.version_code = '" + versionCode + "'");
			}
			if (belongProject != null && !"".equals(belongProject)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("c.belong_project='" + belongProject + "'");
			}
			if (upType != null && !"".equals(upType)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("c.up_type='" + upType + "'");
			}
			if (versionType != null && !"".equals(versionType)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("c.version_type='" + versionType + "'");
			}
			if (status != null && !"".equals(status)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("c.status='" + status + "'");
			}

			ProjectInfo pro = new ProjectInfo();
			List<DataMap> pros = ServiceBean.getInstance()
					.getProjectInfoFacade().getProjectInfo(pro);
			request.setAttribute("project", pros);

			request.setAttribute("fNow_date", startTime);
			request.setAttribute("now_date", endTime);
			request.setAttribute("versionCode", versionCode);
			request.setAttribute("belongProject", belongProject);
			request.setAttribute("upType", upType);
			request.setAttribute("versionType", versionType);
			request.setAttribute("status", status);

			vo.setCondition(sb.toString());

			BeanUtils.copyProperties(vo, form);
			list = info.getCheckInfoListByVo(vo);
			BeanUtils.copyProperties(pys, form);
			pys.setCounts(list.getTotalSize());

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(Config.ABOUT_PAGE);
			if (e instanceof SystemException) {
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else {
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
			request.setAttribute("pageList", list);
			request.setAttribute("PagePys", pys);
		}
		CommUtils.getIntervalTime(start, new Date(), href);
		return mapping.findForward("queryCheckInfo");
	}

	public ActionForward initInsert(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return mapping.findForward("addIDCode");
	}

	public ActionForward addIDCode(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CheckInfoForm form = (CheckInfoForm) actionForm;
		Result result = new Result();
		try {
			CheckInfo vo = new CheckInfo();
			vo.setIDCode(form.getIDCode());
			ServiceBean.getInstance().getCheckInfoFacade()
					.insertCheckCodeInfo(vo);

			result.setBackPage(HttpTools.httpServletPath(request,
					"queryCheckInfo"));
			result.setResultCode("inserts");
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(request.getQueryString() + "  " + e);
			result.setBackPage(Config.ABOUT_PAGE);
			if (e instanceof SystemException) {
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else {
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}

	public ActionForward uploadCheckInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Date start = new Date();
		String href = request.getServletPath();
		Result result = new Result();
		try {
			CheckInfo vo = new CheckInfo();
			List<DataMap> IDCodeList = ServiceBean.getInstance()
					.getCheckInfoFacade().getCheckCodeInfo(vo);
			request.setAttribute("IDCodeList", IDCodeList);

			ProjectInfo projectInfo = new ProjectInfo();
			List<DataMap> projectList = ServiceBean.getInstance()
					.getProjectInfoFacade().getProjectInfo(projectInfo);
			request.setAttribute("projectList", projectList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(Config.ABOUT_PAGE);/* 这里为管理页面，所以出错后跳转到系统默认页面 */
			if (e instanceof SystemException) {/* 对已知异常进行解析 */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else {/* 对未知异常进行解析，并全部定义成未知异常 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		CommUtils.getIntervalTime(start, new Date(), href);
		return mapping.findForward("uploadCheckInfo");

	}
	
	/**  上传zip文件
	 * @author 张
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward uploadZip(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Date start = new Date();
		String href = request.getServletPath();
		Result result = new Result();
		try {
			CheckInfo vo = new CheckInfo();
//			List<DataMap> IDCodeList = ServiceBean.getInstance()
//					.getCheckInfoFacade().getCheckCodeInfo(vo);
//			System.err.println("IDCodeList:"+IDCodeList);
//			request.setAttribute("IDCodeList", IDCodeList);

			ProjectInfo projectInfo = new ProjectInfo();
			List<DataMap> projectList = ServiceBean.getInstance()
					.getProjectInfoFacade().getProjectInfo(projectInfo);
			System.err.println("projectList:"+projectList);
			request.setAttribute("projectList", projectList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(Config.ABOUT_PAGE);/* 这里为管理页面，所以出错后跳转到系统默认页面 */
			if (e instanceof SystemException) {/* 对已知异常进行解析 */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else {/* 对未知异常进行解析，并全部定义成未知异常 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		CommUtils.getIntervalTime(start, new Date(), href);
		return mapping.findForward("uploadZip");

	}

	public ActionForward updateCheckInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		String apkPath = request.getParameter("downloadPath");
		// String belongProject = request.getParameter("project");

		try {
			CheckInfoForm form = (CheckInfoForm) actionForm;
			CheckInfo vo = new CheckInfo();
			CheckInfo voU = new CheckInfo();
			BeanUtils.copyProperties(vo, form);
			String versionType = form.getVersionType();
			String IDCodeId = form.getIDCodeId();
			String belongProject = form.getBelongProject();
			String versionName=form.getVersionName();
			String packageName=form.getPackageName();
			int versionCode=form.getVersionCode();
			String functionCap=form.getFunctionCap();
			System.out.println("wei------------"+versionName+packageName+versionCode+functionCap);
			String type = "";
			String name = "";
			if (versionType.equals("0")) {
				type = "inner";
			} else if (versionType.equals("1")) {
				type = "outer";
			} else {
				type = "qc";
			}
			if ("".equals(CommUtils.getSubStr(apkPath, 2))) {
				apkPath = "http://" + getServerName() + ":"
						+ request.getServerPort() + request.getContextPath()
						+ "/upload/apk/" + type + "/" + belongProject + "/"
						+ apkPath;
			} else {
				apkPath = "http://" + getServerName() + ":"
						+ request.getServerPort() + request.getContextPath()
						+ "upload/apk/" + type + "/" + belongProject + "/"
						+ CommUtils.getSubStr(apkPath, 2);
			}

			String interPath = "";
			String path = "";
			Hashtable<?, ?> files = form.getMultipartRequestHandler()
					.getFileElements();// 获取所有文件路径的枚举；
			if (files != null & files.size() > 0) {
				Enumeration<?> enums = files.keys();
				String fileKey = null;
				String dir = request
						.getSession(true)
						.getServletContext()
						.getRealPath(
								"/upload/apk/" + type + "/" + belongProject);
				while (enums.hasMoreElements()) {
					fileKey = (String) (enums.nextElement());
					FormFile file = (FormFile) files.get(fileKey);
					System.out.println("文件大小----" + file.getFileSize());
					// name = file.getFileName();
					name = "care.apk";
					if (name != null && !"".equals(name.trim())) {
						CommUtils.createDateFile(dir); // 创建当前文件夹，存在则返回文件名；
						InputStream in = file.getInputStream();
						path = dir + "/" + name; // 输出文件路径
						logger.info("输出文件路径---------" + path);
						File f = new File(path);
						if (f.exists()) {
							f.delete();
						}
						interPath = "http://" + getServerName() + ":"
								+ request.getServerPort()
								+ request.getContextPath() + "/upload/apk/"
								+ type + "/" + belongProject + "/" + name;
						vo.setDownloadPath(interPath);
						logger.info("OutputStream-------前------" + interPath);
						OutputStream out = new FileOutputStream(path);
						logger.info("OutputStream-------后------");
						out.write(file.getFileData(), 0, file.getFileSize());
						logger.info("卫out.write后------");
						in.close();
						in = null;
						out.close();
						out = null;
						logger.info("OutputStream  close后--------");
						logger.info("获取APP信息后");
						vo.setVersionName(versionName);
						vo.setPackageName(packageName);
						vo.setVersionCode(versionCode);

						CheckInfo ci = new CheckInfo();
						ci.setCondition("id='" + IDCodeId
								+ "' order by id desc limit 0,1");
						List<DataMap> ciList = ServiceBean.getInstance()
								.getCheckInfoFacade().getCheckCodeInfo(ci);
						CheckInfo upo = new CheckInfo();
						if (ciList.size() > 0) {
							vo.setUpUser(ciList.get(0).getAt("ID_code") + "");
							upo.setUpUser(ciList.get(0).getAt("ID_code") + "");
						} else {
							vo.setUpUser("-1");
							upo.setUpUser("-1");
						}

						vo.setStatus("1");
						vo.setUploadTime(new Date());

				
						voU.setCondition("package_name='"
								+ packageName
								+ "'and belong_project='" + belongProject + "'and version_type='"+versionType+"'order by version_code");
						List<DataMap> list = ServiceBean.getInstance()
								.getCheckInfoFacade().getCheckInfo(voU);// 查询
						if (list.size() <= 0) {
							ServiceBean.getInstance().getCheckInfoFacade()
									.insertCheckInfo(vo);
						} else {
							upo.setCondition("package_name='"
									+ packageName
									+ "'and belong_project='" + belongProject + "'and version_type='"+versionType+"'order by version_code");
							upo.setVersionCode(versionCode);
							upo.setFunctionCap(functionCap);
							upo.setVersionName(versionName);
							ServiceBean.getInstance().getCheckInfoFacade()
									.updateCheckInfo(upo);
						}
					}
				}

			}

			result.setBackPage(HttpTools.httpServletPath(request,
					"queryCheckInfo"));
			result.setResultCode("save");
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryCheckInfo"));
			if (e instanceof SystemException) {/* 对已知异常进行解析 */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else {/* 对未知异常进行解析，并全部定义成未知异常 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}
	
	/**
	 * 选择zip文件存放的位置
	 * @author 张
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateZip(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		String zipPath = request.getParameter("downloadPath");
		//String belongProject = request.getParameter("project");
		try {
			CheckInfoForm form = (CheckInfoForm) actionForm;
			CheckInfo vo = new CheckInfo();
			BeanUtils.copyProperties(vo, form);
		
			String belongProject = form.getBelongProject();
			String name = "";
			if ("".equals(CommUtils.getSubStr(zipPath, 2))) {
				zipPath = "http://" + getServerName() + ":"
						+ request.getServerPort() + request.getContextPath()
						+ "/upload/zip/" + belongProject + "/"
						+ zipPath;
			} else {
				zipPath = "http://" + getServerName() + ":"
						+ request.getServerPort() + request.getContextPath()
						+ "upload/zip/" + belongProject + "/"
						+ CommUtils.getSubStr(zipPath, 2);
			}

			String interPath = "";
			String path = "";
			Hashtable<?, ?> files = form.getMultipartRequestHandler()
					.getFileElements();// 获取所有文件路径的枚举；
			if (files != null & files.size() > 0) {
				Enumeration<?> enums = files.keys();
				String fileKey = null;
				String dir = request.getSession(true).getServletContext()
						.getRealPath("/upload/zip/" + belongProject);
				while (enums.hasMoreElements()) {
					fileKey = (String) (enums.nextElement());
					FormFile file = (FormFile) files.get(fileKey);
//					name = file.getFileName();
					name= "care.zip";
					if (name != null && !"".equals(name.trim())) {
						File f1 = new File(dir); // 创建当前文件夹，存在则返回文件名；
						if (!f1.exists()) {
							f1.mkdirs();
						} 
						InputStream in = file.getInputStream();
						path = dir + "/" + name; // 输出文件路径
						File f = new File(path);
						if (f.exists()) {
							f.delete();
						}
						interPath = "http://" + getServerName() + ":"
								+ request.getServerPort()
								+ request.getContextPath() + "/upload/zip/"
								 + belongProject + "/" + name;
						//request.getContextPath()  huo
						vo.setDownloadPath(interPath);
						OutputStream out = new FileOutputStream(path);
						System.err.println("path===1"+path);
						out.write(file.getFileData(), 0, file.getFileSize());
						out.close();
						out = null;
						in.close();
					}
				}
				// String dir2 =
				// request.getSession(true).getServletContext().getRealPath("/upload/temp")+"/"+name;//要删除文件的路径；
				// File tempFile = new File(dir2);
				// if(tempFile.exists()){
				// tempFile.delete();
				// }
				// File temp = new
				// File(request.getSession(true).getServletContext().getRealPath("/upload/temp"));
				// if(temp.exists()){
				// temp.delete();
				// }
			}

			// String
			// backMessage=apkInfo.getVersionName()+"@"+apkInfo.getPackageName()+"@"+apkInfo.getVersionCode()+"@"+filename;

//			CheckInfo ci = new CheckInfo();
//
//			vo.setStatus("1");
//			vo.setUploadTime(new Date());
			//检查
			/*voU.setPackageName(apkInfo.getPackageName());//包名
			voU.setVersionName(apkInfo.getVersionName());//版本名
			voU.setVersionType(versionType);//版本类型
			voU.setUpType(form.getUpType());//升级类型
			voU.setBelongProject(belongProject);*/
			vo.setVersionName(form.getVersionName());
			vo.setPackageName(form.getPackageName());
			vo.setVersionCode(form.getVersionCode());
			vo.setBelongProject(belongProject);
			vo.setStatus("1");
			vo.setUploadTime(new Date());
			vo.setFunctionCap(form.getFunctionCap());
			
			CheckInfo upo=new CheckInfo();
			CheckInfo voU = new CheckInfo();
			/*
			 * 先查询一遍数据库 看是否已经存在这个固件，如果存在则更新，不存在则插入
			 */
			upo.setCondition("version_name='"+form.getVersionName()+"' and belong_project="+belongProject+"");
			System.err.println(upo.getCondition());
			List<DataMap> list= ServiceBean.getInstance().getCheckInfoFacade().getFirmDevice(upo);
			System.err.println("list======================="+list.size());
			if(list.size()<=0){
				ServiceBean.getInstance().getCheckInfoFacade().insertFirmDevice(vo);
			}else{
				System.out.println(list.size()+"==========list");
				voU.setCondition("version_name='"+form.getVersionName()
						+"'and belong_project='"+belongProject+"'");
				voU.setVersionCode(form.getVersionCode());
				ServiceBean.getInstance().getCheckInfoFacade().updateFirmDevice(voU);
			}
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryFirmDevice"));
			result.setResultCode("save");
			result.setResultType("success");
		} catch (Exception e) {
			System.out.println("异常了  。。");
			System.out.println(e.getMessage());
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryFirmDevice"));
			if (e instanceof SystemException) {/* 对已知异常进行解析 */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else {/* 对未知异常进行解析，并全部定义成未知异常 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		}finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}
	
	public ActionForward getApkMessage(ActionMapping mapping, ActionForm actionForm,HttpServletRequest request,HttpServletResponse response) {
		CheckInfoForm form=(CheckInfoForm)actionForm;
		String apk = request.getParameter("downloadPath");
		FormFile file = form.getFile1(); // iframe是隐藏窗口，实现文件上传功能,而ajax只是刷新局部，不能实现上传功能

		try {
			String dir = request.getSession(true).getServletContext()
					.getRealPath("/upload/apk");
			String tempPath = dir.replace("apk", "temp");
			File temp = new File(tempPath);
			temp.mkdir();
			if (apk != null && !"".equals(apk)) {
				String delPath = tempPath + "/" + CommUtils.getSubStr(apk, 1);
				File f = new File(delPath);
				if (f.exists()) {
					f.delete();
				}
			}
			String filename = file.getFileName();

			String path = tempPath + "/" + filename;
			OutputStream fos = new FileOutputStream(path); // 这是必须有的，只有这样才把apk输出到upload上
			fos.write(file.getFileData(), 0, file.getFileSize());
			fos.flush();
			fos.close();
			ApkUtil apkUtil = new ApkUtil();
			String anOSName = System.getProperty("os.name");
			if (anOSName.toLowerCase().startsWith("windows")) {
				apkUtil.setmAaptPath(request.getSession(true)
						.getServletContext().getRealPath("/WEB-INF")
						+ "/lib/aapt.exe");
			} else {
				apkUtil.setmAaptPath(request.getSession(true)
						.getServletContext().getRealPath("/WEB-INF")
						+ "/lib/aapt");
			}
			com.sinaapp.msdxblog.apkUtil.entity.ApkInfo apkInfo = apkUtil
					.getApkInfo(path);

			String backMessage = apkInfo.getVersionName() + "@"
					+ apkInfo.getPackageName() + "@" + apkInfo.getVersionCode()
					+ "@" + filename;
			response.getWriter().write(
					"<script>parent.callback('" + backMessage + "');</script>"); // iframe是子窗口,回传参数给父窗口,用parent
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ActionForward downloadApk(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Result result = new Result();
		boolean isOnLine = true;
		try {
			String downloadUrl = request.getParameter("download");
			String versionType = request.getParameter("versionType");
			String IDCode = request.getParameter("IDCode");
			String type = "";
			if (versionType.equals("0")) {
				type = "inner";
			} else {
				type = "outer";
			}

			String dir = request.getSession(true).getServletContext()
					.getRealPath("/upload/apk/" + type + "/" + IDCode);
			String apkname = downloadUrl
					.substring(downloadUrl.lastIndexOf("/") + 1);
			logger.info("下载地址-------------------------" + dir + "/" + apkname);
			File f = new File(dir + "/" + apkname);
			if (!f.exists()) {
				throw new SystemException("fail", "noAPKPath");
			}
			FileInputStream fin = new FileInputStream(f);
			BufferedInputStream br = new BufferedInputStream(fin);
			byte[] buf = new byte[1024 * 8];
			int len = 0;

			response.reset();
			if (!isOnLine) {
				URL u = new URL("file:///" + dir);
				response.setContentType(u.openConnection().getContentType());
				response.setHeader("Content-Disposition", "inline; filename="
						+ apkname);
				// 文件名应该编码成UTF-8
			} else { // 纯下载方式
				response.setContentType("application/x-msdownload");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + f.getName());
			}
			OutputStream out = response.getOutputStream();
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			br.close();
			out.close();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryCheckInfo"));
			if (e instanceof SystemException) { /* 对已知异常进行解析 */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* 对未知异常进行解析，并全部定义成未知异常 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}
	
	/**
	 *  下载zip文件
	 * @author 张
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward downloadZip(ActionMapping mapping, ActionForm actionForm,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Result result = new Result();
		boolean isOnLine = true;
		try {
			
			String downloadUrl = request.getParameter("download");
			String belongProject = request.getParameter("belong_project");
			System.err.println("downloadUrl=============="+downloadUrl);
			System.err.println("belongProject=============="+belongProject);
			
			String dir = request.getSession(true).getServletContext().getRealPath("/upload/zip/" + belongProject );
			System.err.println("dir=============="+dir);
			String zipName = downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
			System.err.println("zipName=============="+zipName);
			
			 File f = new File(dir+"/"+zipName);		 
		        if (!f.exists()) {
		            throw new SystemException("fail","noAPKPath");
		        }
		        FileInputStream fin = new FileInputStream(f);
		        BufferedInputStream br = new BufferedInputStream(fin);
		        byte[] buf = new byte[1024*8];
		        int len = 0;

		        response.reset(); 
		        if (!isOnLine) { 
		            URL u = new URL("file:///" + dir);
		            response.setContentType(u.openConnection().getContentType());
		            response.setHeader("Content-Disposition", "inline; filename=" + zipName);
		            // 文件名应该编码成UTF-8
		        } else { // 纯下载方式
		        	response.setCharacterEncoding("utf-8");
		            response.setContentType("application/x-msdownload;charset=utf-8");
		            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
		        }
		        OutputStream out = response.getOutputStream();
		        while ((len = br.read(buf)) > 0)
		            out.write(buf, 0, len);
		        br.close();
		        out.close();
		        return null;
		}catch (Exception e) {
			e.printStackTrace();
			result.setBackPage(HttpTools.httpServletPath(request,"queryFirmDevice"));
			if (e instanceof SystemException) { /* 对已知异常进行解析 */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* 对未知异常进行解析，并全部定义成未知异常 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		}finally {
			request.setAttribute("result", result);				
			}
		return mapping.findForward("result");
	}
	
	
	public String getServerName() throws Exception {
		String serverName = "";
		Properties pros = new Properties();
		pros.load(this.getClass().getClassLoader()
				.getResourceAsStream("server.properties"));
		serverName = pros.getProperty("servername");
		return serverName;
	}

	public ActionForward getCheckInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		CheckInfoFacade info = ServiceBean.getInstance().getCheckInfoFacade();
		String belongProject = request.getParameter("belongProject");
		List<DataMap> list = null;
		try {
			CheckInfoForm form = (CheckInfoForm) actionForm;
			CheckInfo vo = new CheckInfo();
			BeanUtils.copyProperties(vo, form);
			if (belongProject != null && !"".equals(belongProject)) {
				vo.setCondition("belong_project='" + belongProject + "'");
			}
			list = info.getCheckInfo(vo);
			/* 设置化排序字段 */
			String result = "";
			if (list.size() > 0) {
				result = list.get(0).getAt("version_name") + "@"
						+ list.get(0).getAt("package_name") + "@"
						+ list.get(0).getAt("version_code") + "@"
						+ list.get(0).getAt("function_cap") + "@"
						+ list.get(0).getAt("download_path");
			}
			response.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	public ActionForward updateStatus(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Result result = new Result();
		try {
			String status = request.getParameter("status");
			String id = request.getParameter("id");
			CheckInfo vo = new CheckInfo();
			vo.setStatus(status);
			vo.setCondition("id='" + id + "'");
			ServiceBean.getInstance().getCheckInfoFacade().updateCheckInfo(vo);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryCheckInfo"));
			if (status.equals("3")) {
				result.setResultCode("deletes");
			} else {
				result.setResultCode("updates");
			}
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryCheckInfo"));
			if (e instanceof SystemException) { /* 对已知异常进行解析 */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* 对未知异常进行解析，并全部定义成未知异常 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}

	public ActionForward initUpdateRemarks(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		CheckInfo vo = new CheckInfo();
		vo.setCondition("id=" + id);

		List<DataMap> list = ServiceBean.getInstance().getCheckInfoFacade()
				.getCheckInfo(vo);
		if (list == null || list.size() == 0) {
			Result result = new Result();
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryCheckInfo"));
			result.setResultCode("rowDel");
			result.setResultType("success");
			return mapping.findForward("result");
		}
		request.setAttribute("checkInfo", list.get(0));
		return mapping.findForward("updateRemarks");
	}
	
	/**
	 * 
	 * @author 张
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward initFirmUpdateRemarks(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String id = request.getParameter("id");
		CheckInfo vo = new CheckInfo();
		vo.setCondition("id="+id);
		
		List<DataMap> list = ServiceBean.getInstance().getCheckInfoFacade().getFirmDevice(vo);
		if (list == null || list.size() == 0) {
			Result result = new Result();
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryFirmDevice"));
			result.setResultCode("rowDel");
			result.setResultType("success");
			return mapping.findForward("result");
		}
		request.setAttribute("firmDevice", list.get(0));
		return mapping.findForward("updateFirmRemarks");
	}
	
	/**
	 * @author 张
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateFirmRemarks(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		try {
			System.err.println("hello");
			CheckInfoForm form = (CheckInfoForm) actionForm;
			CheckInfo vo = new CheckInfo();
			BeanUtils.copyProperties(vo, form);
			int id = form.getId();
			vo.setCondition("id="+id);
			List<DataMap> ciList = ServiceBean.getInstance()
					.getCheckInfoFacade().getFirmDevice(vo);
			System.err.println(ciList.size());
			
			if(ciList.size()>0){
				System.err.println(ciList.size());
			String belongProject = ciList.get(0).getAt("belong_project")+"";
//			String versionName = ciList.get(0).getAt("version_name")+"";
//			String versionCode= ciList.get(0).getAt("version_code")+"";
			String functionCap =form.getFunctionCap();
			String remarks =form.getRemarks();
			String strs = request
					.getSession(true)
					.getServletContext()
					.getRealPath(
							"/upload/zip/"+belongProject+"/care.zip");
//				String strsQc = request
//						.getSession(true)
//						.getServletContext()
//						.getRealPath(
//								"/upload/apk/qc/1/care.apk");
				File f = new File(strs);
				if (!f.exists()) {
					result.setResultCode("notFoundFileException");
				}else{
					CheckInfo cvo = new CheckInfo();
					cvo.setFunctionCap(functionCap);
					cvo.setRemarks(remarks);
					cvo.setCondition("id='"+id+"'");
					int i=ServiceBean.getInstance().getCheckInfoFacade().updateFirmDevice(cvo);
					if(i!=1){
						result.setResultCode("更新失败");
					}else{
						result.setResultCode("updates");
						result.setResultType("success");
					}
				}
//				File fQc = new File(strsQc);
//				if (fQc.exists()) {
//					fQc.delete();
//				}

		
		}
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryFirmDevice"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryFirmDevice"));
			if (e instanceof SystemException) { /* 对已知异常进行解析 */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* 对未知异常进行解析，并全部定义成未知异常 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}
	
	
	public ActionForward updateRemarks(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		try {
			CheckInfoForm form = (CheckInfoForm) actionForm;
			CheckInfo vo = new CheckInfo();
			BeanUtils.copyProperties(vo, form);
			String erWeiMa = form.getErWeiMa();
			int id = form.getId();
			vo.setCondition("id='" + form.getId() + "'");
			// 修改二维码的状态
			ServiceBean.getInstance().getCheckInfoFacade().updateCheckInfo(vo);
			List<DataMap> ciList = ServiceBean.getInstance()
					.getCheckInfoFacade().getCheckInfo(vo);
			if (erWeiMa != null && erWeiMa.equals("1")) {
				vo.setErWeiMa(erWeiMa);
				if (ciList.size() > 0) {
					String IdCodeId = (String) ciList.get(0)
							.getAt("ID_code_id");
					String dir = request.getSession(true).getServletContext()
							.getRealPath("/upload/apk/outer/" + IdCodeId);
					String dirQc = request.getSession(true).getServletContext()
							.getRealPath("/upload/apk/qc/1");
					String downloadPath = (String) ciList.get(0).getAt(
							"download_path");
					String downloadPathChange = downloadPath.replaceAll(
							"inner", "outer");
					String downloadPathQc = downloadPath.replaceAll("inner",
							"qc");
					Constant.createFile(dir);
					Constant.createFile(dirQc);
					/* 指定源文件的存放路径 */
					String str = request
							.getSession(true)
							.getServletContext()
							.getRealPath(
									"/upload/apk/inner/" + IdCodeId
											+ "/care.apk");
					/* 指定复制后的文件的目标路径 */
					String strs = request
							.getSession(true)
							.getServletContext()
							.getRealPath(
									"/upload/apk/outer/" + IdCodeId
											+ "/care.apk");
					/* 指定复制后的文件的目标路径 */
					String strsQc = request
							.getSession(true)
							.getServletContext()
							.getRealPath(
									"/upload/apk/qc/1/care.apk");
					/* 创建输入和输出流 */
					FileInputStream fis = null;
					FileOutputStream fos = null;

					try {
						/* 将io流和文件关联 */
						fis = new FileInputStream(str);

						fos = new FileOutputStream(strs);
						byte[] buf = new byte[1024 * 1024];
						int len;
						while ((len = fis.read(buf)) != -1) {
							fos.write(buf, 0, len);
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						try {
							fis.close();
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						/* 将io流和文件关联 */
						fis = new FileInputStream(str);

						fos = new FileOutputStream(strsQc);
						byte[] buf = new byte[1024 * 1024];
						int len;
						while ((len = fis.read(buf)) != -1) {
							fos.write(buf, 0, len);
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						try {
							fis.close();
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					String packageName = (String) ciList.get(0).getAt(
							"package_name");
					CheckInfo so = new CheckInfo();
					so.setCondition("package_name='" + packageName + "'"
							+ "AND download_path='" + downloadPathChange + "'");
					int count = ServiceBean.getInstance().getCheckInfoFacade()
							.getCheckInfoCount(so);
					CheckInfo qco = new CheckInfo();
					qco.setCondition("package_name='" + packageName + "'"
							+ "AND download_path='" + downloadPathQc + "'");
					int countQc = ServiceBean.getInstance()
							.getCheckInfoFacade().getCheckInfoCount(qco);
					if (count > 0) {
					} else {
						CheckInfo co = new CheckInfo();
						co.setPackageName((String) ciList.get(0).getAt(
								"package_name"));
						co.setVersionName((String) ciList.get(0).getAt(
								"version_name"));
						co.setVersionCode(Integer.parseInt((String) ciList.get(
								0).getAt("version_code")));
						co.setDownloadPath(downloadPathChange);
						co.setFunctionCap((String) ciList.get(0).getAt(
								"function_cap"));
						co.setBelongProject(String.valueOf(ciList.get(0).getAt(
								"belong_project")));
						co.setIDCodeId((String) ciList.get(0).getAt(
								"ID_code_id"));
						co.setUpType((String) ciList.get(0).getAt("up_type"));
						co.setUpPlatform((String) ciList.get(0).getAt(
								"up_platform"));
						co.setStatus((String) ciList.get(0).getAt("status"));
						co.setUpUser((String) ciList.get(0).getAt("up_user"));
						co.setRemarks((String) ciList.get(0).getAt("remarks"));
						co.setVersionType("1");
						co.setUploadTime(new Date());
						co.setErWeiMa(erWeiMa);
						// 添加到数据库
						ServiceBean.getInstance().getCheckInfoFacade()
								.insertCheckInfo(co);
					}
					if (countQc > 0) {

					} else {
						CheckInfo qo = new CheckInfo();
						qo.setPackageName((String) ciList.get(0).getAt(
								"package_name"));
						qo.setVersionName((String) ciList.get(0).getAt(
								"version_name"));
						qo.setVersionCode(Integer.parseInt((String) ciList.get(
								0).getAt("version_code")));
						qo.setDownloadPath(downloadPathQc);
						qo.setFunctionCap((String) ciList.get(0).getAt(
								"function_cap"));
						qo.setBelongProject(String.valueOf(ciList.get(0).getAt(
								"belong_project")));
						qo.setIDCodeId((String) ciList.get(0).getAt(
								"ID_code_id"));
						qo.setUpType((String) ciList.get(0).getAt("up_type"));
						qo.setUpPlatform((String) ciList.get(0).getAt(
								"up_platform"));
						qo.setStatus((String) ciList.get(0).getAt("status"));
						qo.setUpUser((String) ciList.get(0).getAt("up_user"));
						qo.setRemarks((String) ciList.get(0).getAt("remarks"));
						qo.setVersionType("2");
						qo.setUploadTime(new Date());
						qo.setErWeiMa(erWeiMa);
						// 添加到数据库
						ServiceBean.getInstance().getCheckInfoFacade()
								.insertCheckInfo(qo);
					}
				} else {
					System.out.println("error");
				}
			} else if (erWeiMa != null && erWeiMa.equals("0")) {
				vo.setErWeiMa(erWeiMa);
				String IdCodeId = (String) ciList.get(0).getAt("ID_code_id");
				String strs = request
						.getSession(true)
						.getServletContext()
						.getRealPath(
								"/upload/apk/outer/" + IdCodeId + "/care.apk");
				String strsQc = request.getSession(true).getServletContext()
						.getRealPath("/upload/apk/qc/1/care.apk");
				File f = new File(strs);
				if (f.exists()) {
					f.delete();
				}
				File fQc = new File(strsQc);
				if (fQc.exists()) {
					fQc.delete();
				}

				CheckInfo del = new CheckInfo();
				String packageName = (String) ciList.get(0).getAt(
						"package_name");
				String downloadPath = (String) ciList.get(0).getAt(
						"download_path");
				String downloadPathChange = downloadPath.replaceAll("inner",
						"outer");
				del.setCondition("package_name='" + packageName + "'"
						+ "AND download_path='" + downloadPathChange + "'");
				ServiceBean.getInstance().getCheckInfoFacade()
						.deleteCheckInfo(del);

				CheckInfo delQc = new CheckInfo();
				String packageNameQc = (String) ciList.get(0).getAt(
						"package_name");
				String downloadPathQc = (String) ciList.get(0).getAt(
						"download_path");
				String downloadPathChangeQc = downloadPathQc.replaceAll(
						"inner", "qc");
				delQc.setCondition("package_name='" + packageNameQc + "'"
						+ "AND download_path='" + downloadPathChangeQc + "'");
				ServiceBean.getInstance().getCheckInfoFacade()
						.deleteCheckInfo(delQc);

			}

			result.setBackPage(HttpTools.httpServletPath(request,
					"queryCheckInfo"));
			result.setResultCode("updates");
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryCheckInfo"));
			if (e instanceof SystemException) { /* 对已知异常进行解析 */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* 对未知异常进行解析，并全部定义成未知异常 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}

	public ActionForward initUpdateUpUser(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		String userName = request.getParameter("userName");
		CheckInfo checkInfo = new CheckInfo();
		checkInfo.setCondition("id='" + id + "'");
		List<DataMap> checkList = ServiceBean.getInstance()
				.getCheckInfoFacade().getCheckInfo(checkInfo);
		String userId = "-1";
		DataList userList = new DataList();
		if (checkList.size() > 0) {
			userId = (String) checkList.get(0).getAt("up_user");
			if (!userId.equals("-1")) {
				String[] userIds = userId.split(",");
				AppUserInfo vo = new AppUserInfo();
				for (int i = 0; i < userIds.length; i++) {
					vo.setCondition("id='" + userIds[i] + "'");
					List<DataMap> user = ServiceBean.getInstance()
							.getAppUserInfoFacade().getAppUserInfo(vo);
					if (user.size() > 0) {
						DataMap map = user.get(0);
						userList.add(map);
					}
				}

			}
		}
		if (userName != null) {
			userName = " ";
		}
		AppUserInfo vo = new AppUserInfo();
		vo.setCondition("user_name like '%" + userName + "%'");
		List<DataMap> appUserInfo = ServiceBean.getInstance()
				.getAppUserInfoFacade().getAppUserInfo(vo);
		request.setAttribute("appUserInfo", appUserInfo);

		// AppUserInfo vo = new AppUserInfo();
		// List<DataMap> list =
		// ServiceBean.getInstance().getAppUserInfoFacade().getAppUserInfo(vo);
		// DataList appUserList = new DataList(list);
		request.setAttribute("userNameList", userList);
		request.setAttribute("id", id);
		return mapping.findForward("updateUpUser");
	}

	public ActionForward updateUpUser(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		try {
			String id = request.getParameter("id");

			String[] userId = request.getParameterValues("userId");
			StringBuffer sb = new StringBuffer();
			if (userId != null) {
				for (int i = 0; i < userId.length; i++) {
					sb.append(userId[i]);
					if (i != userId.length - 1) {
						sb.append(",");
					}
				}
			}

			CheckInfo vo = new CheckInfo();
			if (sb.length() > 0) {
				vo.setUpUser(sb.toString());
			} else {
				vo.setUpUser("-1");
			}

			vo.setCondition("id='" + id + "'");
			ServiceBean.getInstance().getCheckInfoFacade().updateCheckInfo(vo);

			result.setBackPage(HttpTools.httpServletPath(request,
					"queryCheckInfo"));
			result.setResultCode("updates");
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryCheckInfo"));
			if (e instanceof SystemException) { /* 对已知异常进行解析 */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* 对未知异常进行解析，并全部定义成未知异常 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}
	
	public ActionForward getAppUserName(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		
		String userName = request.getParameter("userName");		
		
		try {
			AppUserInfo vo = new AppUserInfo();
			vo.setCondition("user_name ='" +userName+ "'");
			List<DataMap> list = ServiceBean.getInstance().getAppUserInfoFacade().getAppUserInfo(vo);
			StringBuffer sb = new StringBuffer();
			if(list.size() > 0){				
				sb.append(list.get(0).getAt("id"));
				sb.append(",");
				sb.append(list.get(0).getAt("user_name"));				
			}
			
			response.getWriter().write(sb.toString());
			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		return null;
	}
	
	/**
	 * 固件升级，查询固件升级的所有版本
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return  
	 * @throws Exception
	 */
	public ActionForward queryFirmDevice(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Date start = new Date();
		String href = request.getServletPath();
		Result result = new Result();
		PagePys pys = new PagePys();
		DataList list = null;
		StringBuffer sb = new StringBuffer();
		CheckInfoFacade info = ServiceBean.getInstance().getCheckInfoFacade();
		try {
			CheckInfoForm form = (CheckInfoForm) actionForm;
			CheckInfo vo = new CheckInfo();
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String versionCode = request.getParameter("versionCode");
			String belongProject = request.getParameter("belongProject");
//			String upType = request.getParameter("upType");
//			String versionType = request.getParameter("versionType");
			String status = request.getParameter("status");

			LoginUser loginUser = (LoginUser) request.getSession()
					.getAttribute(Config.SystemConfig.LOGINUSER);
			if (loginUser == null) {
				return null;
			}
			String companyInfoId = loginUser.getCompanyId();
			String projectInfoId = loginUser.getProjectId();

			form.setOrderBy("c.upload_time");
			form.setSort("1");

			if (!"0".equals(projectInfoId)) { // projectInfoId.equals("0")
				sb.append("c.belong_project in(" + projectInfoId + ")");
				System.err.println(sb.toString());
			} else {
				if (!"0".equals(companyInfoId)) { // companyInfoId
					ProjectInfo pro = new ProjectInfo();
					pro.setCondition("company_id in(" + companyInfoId + ")");
					List<DataMap> proList = ServiceBean.getInstance()
							.getProjectInfoFacade().getProjectInfo(pro);
					if (proList.size() > 0) {
						int num = proList.size();
						String str = "";
						for (int i = 0; i < num; i++) {
							Integer id = (Integer) proList.get(i).getAt("id");
							str += String.valueOf(id);
							if (i != num - 1) {
								str += ",";
							}
						}
						sb.append("c.belong_project in(" + str + ")");
						System.err.println(sb.toString());
					}
				}
			}

			if (startTime != null && !"".equals(startTime)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("substring(upload_time,1,10) >= '" + startTime + "'");
				System.err.println(sb.toString());
			}
			if (endTime != null && !"".equals(endTime)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("substring(upload_time,1,10) <= '" + endTime + "'");
				System.err.println(sb.toString());
			}
			if (versionCode != null && !"".equals(versionCode)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("c.version_code = '" + versionCode + "'");
				System.err.println(sb.toString());
			}
			if (belongProject != null && !"".equals(belongProject)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("c.belong_project='" + belongProject + "'");
				System.err.println(sb.toString());
			}
//			if (upType != null && !"".equals(upType)) {
//				if (sb.length() > 0) {
//					sb.append(" and ");
//				}
//				sb.append("c.up_type='" + upType + "'");
//				System.err.println(sb.toString());
//			}
//			if (versionType != null && !"".equals(versionType)) {
//				if (sb.length() > 0) {
//					sb.append(" and ");
//				}
//				sb.append("c.version_type='" + versionType + "'");
//				System.err.println(sb.toString());
//			}
			if (status != null && !"".equals(status)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("c.status='" + status + "'");
				System.err.println(sb.toString());
			}

			System.err.println(sb.toString());
			ProjectInfo pro = new ProjectInfo();
			List<DataMap> pros = ServiceBean.getInstance()
					.getProjectInfoFacade().getProjectInfo(pro);
			request.setAttribute("project", pros);

			request.setAttribute("fNow_date", startTime);
			request.setAttribute("now_date", endTime);
			request.setAttribute("versionCode", versionCode);
			request.setAttribute("belongProject", belongProject);
//			request.setAttribute("upType", upType);
//			request.setAttribute("versionType", versionType);
			request.setAttribute("status", status);

			vo.setCondition(sb.toString());

			BeanUtils.copyProperties(vo, form);
			list = info.getFirmDeviceListByVo(vo);
			BeanUtils.copyProperties(pys, form);
			pys.setCounts(list.getTotalSize());

		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(Config.ABOUT_PAGE);
			if (e instanceof SystemException) {
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else {
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
			request.setAttribute("pageList", list);
			request.setAttribute("PagePys", pys);
		}
		CommUtils.getIntervalTime(start, new Date(), href); 
		
		
		
		return mapping.findForward("queryFirmDevice");
		
	}
	
	/**
	 * 
	 * @author 张
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward getZipMessage(ActionMapping mapping, ActionForm actionForm,HttpServletRequest request,HttpServletResponse response) {
	
		 
		CheckInfoForm form=(CheckInfoForm)actionForm;
		String apk = request.getParameter("downloadPath");
		System.err.println("downloadpaht========"+apk);
		String versionCode = request.getParameter("versionCode");
		FormFile file=form.getFile1();   //iframe是隐藏窗口，实现文件上传功能,而ajax只是刷新局部，不能实现上传功能
		
		try {
			String dir = request.getSession(true).getServletContext().getRealPath("/upload/zip");
			if(apk != null && !"".equals(apk)){
				String delPath = dir + "/"+CommUtils.getSubStr(apk, 1);
				File f = new File(delPath);
				if(f.exists()){
					f.delete();
				}
			}		
//			dir = dir.replace("\\upload", "\\temp");
//			CommUtils.createDateFile(dir);
//			System.err.println(dir+"====dir========");
//			String filename = file.getFileName();
//			String fn = filename.substring(0,filename.length()-4);	
//			String ext = filename.substring(filename.lastIndexOf("."),filename.length());	
//			String path = dir + "/"+"/" + fn+ext;
//			System.err.println("path==========="+path);
//			OutputStream fos = new FileOutputStream(path);   //这是必须有的，只有这样才把apk输出到upload上
//			fos.write(file.getFileData(), 0, file.getFileSize());
//			fos.flush();
//			fos.close();
			int code = Integer.valueOf(versionCode) + 1;
//			String backMessage=apks+"@"+String.valueOf(code);
			response.getWriter().write("<script>parent.callback('操作成功');</script>");  //iframe是子窗口,回传参数给父窗口,用parent
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return null;
		
	}
	
}