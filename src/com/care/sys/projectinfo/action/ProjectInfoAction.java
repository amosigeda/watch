package com.care.sys.projectinfo.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.ecs.xhtml.u;
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
import com.care.sys.companyinfo.domain.CompanyInfo;
import com.care.sys.projectinfo.domain.ProjectInfo;
import com.care.sys.projectinfo.domain.logic.ProjectInfoFacade;
import com.care.sys.projectinfo.form.ProjectInfoForm;
import com.godoing.rose.http.common.HttpTools;
import com.godoing.rose.http.common.PagePys;
import com.godoing.rose.http.common.Result;
import com.godoing.rose.lang.DataList;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.lang.SystemException;
import com.godoing.rose.log.LogFactory;

public class ProjectInfoAction extends BaseAction {

	Log logger = LogFactory.getLog(ProjectInfoAction.class);
     String xmlfileName = "advertising.xml";
	//String xmlpath="E:/resin/resin-pro-4.0.53/webapps/ads/WIITE/C7/ads/";
	String xmlpath="/usr/local/resin-pro-4.0.53/webapps/ads/WIITE/C7/ads/";
  	//	String photoPath="E:/resin/resin-pro-4.0.53/webapps/ads/photo/";
	//String photoUrl="http://localhost:8080/ads/photo/";
	
   String photoPath="/usr/local/resin-pro-4.0.53/webapps/ads/photo/";
  String photoUrl="http://www.wiiteer.com:8999/ads/photo/";
  


	public ActionForward queryProjectInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String href = request.getServletPath();
		Date start = new Date();
		Result result = new Result();// ���
		PagePys pys = new PagePys();// ҳ������
		DataList list = null; // ����ҳ��List ��logic itrate��ȡ��
		StringBuffer sb = new StringBuffer();// �����ַ�����
		ProjectInfoFacade info = ServiceBean.getInstance()
				.getProjectInfoFacade();// ����userApp������ȡ��user�ֵ䣩
		ProjectInfo pro = new ProjectInfo();

		try {
			LoginUser loginUser = (LoginUser) request.getSession()
					.getAttribute(Config.SystemConfig.LOGINUSER);
			if (loginUser == null) {
				return null;
			}
			String companyInfoId = loginUser.getCompanyId();
			String projectInfoId = loginUser.getProjectId();
			ProjectInfoForm form = (ProjectInfoForm) actionForm;
			ProjectInfo vo = new ProjectInfo();
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String companyId = request.getParameter("companyId");
			String userId = request.getParameter("userId");
			String projectId = request.getParameter("projectId");

			/* ���û������ֶ� */
			form.setOrderBy("p.add_time");
			form.setSort("1");
			sb.append("1=1");

			if (!projectInfoId.equals("0")) {
				sb.append(" and p.id in(" + projectInfoId + ")");
			} else {
				if (!"0".equals(companyId) && companyId != null) {
					sb.append(" and p.company_id in(" + companyInfoId + ")");
				}
			}
			if (startTime != null && !"".equals(startTime)) {
				sb.append(" and substring(p.add_time,1,10) >= '" + startTime
						+ "'");
			}
			if (endTime != null && !"".equals(endTime)) {
				sb.append(" and substring(p.add_time,1,10) <= '" + endTime
						+ "'");
			}
			if (companyId != null && !"".equals(companyId)) {
				sb.append(" and p.company_id='" + companyId + "'");
			}
			if (projectId != null && !"".equals(projectId)) {
				sb.append(" and p.id ='" + projectId + "'");
			}
			if (userId != null && !"".equals(userId)) {
				sb.append(" and p.company_id='" + userId + "'");
				pro.setCondition("company_id = '" + userId + "'");
			}
			List<DataMap> pros = ServiceBean.getInstance()
					.getProjectInfoFacade().getProjectInfo(pro);
			request.setAttribute("project", pros);

			CompanyInfo ci = new CompanyInfo();
			List<DataMap> coms = ServiceBean.getInstance()
					.getCompanyInfoFacade().getCompanyInfo(ci);
			request.setAttribute("company", coms);

			request.setAttribute("fNow_date", startTime);
			request.setAttribute("now_date", endTime);
			request.setAttribute("companyId", companyId);
			request.setAttribute("userId", userId);
			request.setAttribute("projectId", projectId);

			vo.setCondition(sb.toString());

			BeanUtils.copyProperties(vo, form);
			list = info.getProjectInfoListByVo(vo);
			BeanUtils.copyProperties(pys, form);
			pys.setCounts(list.getTotalSize());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(request.getQueryString() + "  " + e);
			result.setBackPage(Config.ABOUT_PAGE); /*
													 * ����Ϊ����ҳ�棬���Գ������ת��ϵ
													 * ͳĬ��ҳ��
													 */
			if (e instanceof SystemException) { /* ����֪�쳣���н��� */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* ��δ֪�쳣���н�������ȫ�������δ֪�쳣 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
			request.setAttribute("pageList", list);
			request.setAttribute("PagePys", pys);
		}
		CommUtils.getIntervalTime(start, new Date(), href);
		return mapping.findForward("queryProjectInfo");
	}

	public ActionForward queryProjectInfoXml(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String href = request.getServletPath();
		Date start = new Date();
		Result result = new Result();// ���
		PagePys pys = new PagePys();// ҳ������
		DataList list = null; // ����ҳ��List ��logic itrate��ȡ��
		StringBuffer sb = new StringBuffer();// �����ַ�����
		ProjectInfoFacade info = ServiceBean.getInstance()
				.getProjectInfoFacade();// ����userApp������ȡ��user�ֵ䣩
		ProjectInfo pro = new ProjectInfo();

		try {
			LoginUser loginUser = (LoginUser) request.getSession()
					.getAttribute(Config.SystemConfig.LOGINUSER);
			if (loginUser == null) {
				return null;
			}

			String userName = loginUser.getUserName();

			String companyInfoId = loginUser.getCompanyId();
			String projectInfoId = loginUser.getProjectId();
			ProjectInfoForm form = (ProjectInfoForm) actionForm;
			ProjectInfo vo = new ProjectInfo();
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String companyId = request.getParameter("companyId");
			String userId = request.getParameter("userId");
			String projectId = request.getParameter("projectId");

			/* ���û������ֶ� */
			form.setOrderBy("p.add_time");
			form.setSort("1");
			sb.append("1=1");

			if (!projectInfoId.equals("0")) {
				sb.append(" and p.id in(" + projectInfoId + ")");
			} else {
				if (!"0".equals(companyId) && companyId != null) {
					sb.append(" and p.company_id in(" + companyInfoId + ")");
				}
			}
			if (startTime != null && !"".equals(startTime)) {
				sb.append(" and substring(p.add_time,1,10) >= '" + startTime
						+ "'");
			}
			if (endTime != null && !"".equals(endTime)) {
				sb.append(" and substring(p.add_time,1,10) <= '" + endTime
						+ "'");
			}
			if (companyId != null && !"".equals(companyId)) {
				sb.append(" and p.company_id='" + companyId + "'");
			}
			if (projectId != null && !"".equals(projectId)) {
				sb.append(" and p.id ='" + projectId + "'");
			}
			if (userId != null && !"".equals(userId)) {
				sb.append(" and p.company_id='" + userId + "'");
				pro.setCondition("company_id = '" + userId + "'");
			}
			List<DataMap> pros = ServiceBean.getInstance()
					.getProjectInfoFacade().getProjectInfo(pro);
			request.setAttribute("project", pros);

			CompanyInfo ci = new CompanyInfo();
			List<DataMap> coms = ServiceBean.getInstance()
					.getCompanyInfoFacade().getCompanyInfo(ci);
			request.setAttribute("company", coms);

			request.setAttribute("fNow_date", startTime);
			request.setAttribute("now_date", endTime);
			request.setAttribute("companyId", companyId);
			request.setAttribute("userId", userId);
			request.setAttribute("projectId", projectId);
			if (!"admin".equals(userName)) {
				if (sb.toString().length() > 0) {
					sb.append(" and p.heart_s ='" + userName + "' AND p.project_no='"+userName+"'");
				} else {
					sb.append("p.heart_s ='" + userName + "' AND p.project_no='"+userName+"'");
				}
			}
			vo.setCondition(sb.toString());

			BeanUtils.copyProperties(vo, form);
			list = info.getProjectInfoListByVo(vo);
			BeanUtils.copyProperties(pys, form);
			pys.setCounts(list.getTotalSize());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(request.getQueryString() + "  " + e);
			result.setBackPage(Config.ABOUT_PAGE); /*
													 * ����Ϊ����ҳ�棬���Գ������ת��ϵ
													 * ͳĬ��ҳ��
													 */
			if (e instanceof SystemException) { /* ����֪�쳣���н��� */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* ��δ֪�쳣���н�������ȫ�������δ֪�쳣 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
			request.setAttribute("pageList", list);
			request.setAttribute("PagePys", pys);
		}
		CommUtils.getIntervalTime(start, new Date(), href);
		return mapping.findForward("queryProjectInfoxml");
	}

	public ActionForward initInsert(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String company_name = request.getParameter("company_name");
		CompanyInfo vo = new CompanyInfo();
		List<DataMap> Clist = ServiceBean.getInstance().getCompanyInfoFacade()
				.getCompanyInfo(vo);
		request.setAttribute("companyList", CommUtils.getPrintSelect(Clist,
				"companyName", "company_name", "id", company_name, 1));

		return mapping.findForward("addProjectInfo");
	}

	public ActionForward initInsertxml(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		/*
		 * String company_name = request.getParameter("company_name");
		 * CompanyInfo vo = new CompanyInfo(); List<DataMap> Clist =
		 * ServiceBean.getInstance().getCompanyInfoFacade() .getCompanyInfo(vo);
		 * request.setAttribute("companyList", CommUtils .getPrintSelect(Clist,
		 * "companyName", "company_name", "id", company_name, 1));
		 */
		LoginUser loginUser = (LoginUser) request.getSession()
				.getAttribute(Config.SystemConfig.LOGINUSER);
		if (loginUser == null) {
			return null;
		}

		String userName = loginUser.getUserName();
		if("admin".equals(userName)){
			return mapping.findForward("addProjectInfoxml");
		}else{
			return mapping.findForward("addProjectInfoxmlOther");
		}
	}

	public ActionForward insertProjectInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		String companyId = request.getParameter("companyName");

		Result result = new Result();
		try {

			ProjectInfoForm form = (ProjectInfoForm) actionForm;
			ProjectInfoFacade facade = ServiceBean.getInstance()
					.getProjectInfoFacade();
			ProjectInfo vo = new ProjectInfo();
			int num = ServiceBean.getInstance().getProjectInfoFacade()
					.getProjectInfoCount(vo) + 1;
			BeanUtils.copyProperties(vo, form);
			vo.setId(num);
			vo.setProjectNo(form.getProjectNo() + Constant.SPLITE + num);
			vo.setCompanyId(companyId);
			vo.setAddTime(new Date());
			vo.setStatus("1");
			facade.insertProjectInfo(vo);

			result.setBackPage(HttpTools.httpServletPath(request, // ����ɹ�����ת��ԭ��ҳ��
					"queryProjectInfo"));
			result.setResultCode("inserts"); // ���ò���Code
			result.setResultType("success"); // ���ò���ɹ�
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request, "initInsert"));
			if (e instanceof SystemException) { /* ����֪�쳣���н��� */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* ��δ֪�쳣���н�������ȫ�������δ֪�쳣 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}

	public ActionForward insertProjectInfoxml(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
	//	String channelId = request.getParameter("channelId");
		Result result = new Result();
		String name = "";
		String fileFormat = "";
		try {
			LoginUser loginUser = (LoginUser) request.getSession()
					.getAttribute(Config.SystemConfig.LOGINUSER);
			if (loginUser == null) {
				return null;
			}
			ProjectInfoForm form = (ProjectInfoForm) actionForm;
			System.out.println(form.getChannelId());
			Hashtable<?, ?> files = form.getMultipartRequestHandler().getFileElements();//获取所有文件路径的枚举；
			if (files != null & files.size() > 0) {
				Enumeration<?> enums = files.keys();
				String fileKey = null;					
				while (enums.hasMoreElements()) {
					fileKey = (String) (enums.nextElement());
					FormFile file = (FormFile) files.get(fileKey);
					if(!file.getFileName().isEmpty())
					{
						fileFormat = file.toString().substring(file.toString().lastIndexOf("."),file.toString().length());
						name=Long.toString(new Date().getTime())+fileFormat;
						//CommUtils.createDateFile(dir); //创建当前文件夹，存在则返回文件名；
						InputStream in = file.getInputStream();
						//photoPath =  photoPath  + name;   //输出文件路径
						System.out.println(photoPath  + name);
						File f = new File(photoPath  + name);
						if(f.exists()){
							f.delete();	
						}
						
						OutputStream out = new FileOutputStream( photoPath  + name);
						out.write(file.getFileData(), 0, file.getFileSize());
							
						out.close();
						out = null;				
						in.close();		
					}
					
				}
		
			
			}
			
			StringBuffer sb = new StringBuffer();
			String userName = loginUser.getUserName();
			//ProjectInfoForm form = (ProjectInfoForm) actionForm;
			ProjectInfoFacade facade = ServiceBean.getInstance()
					.getProjectInfoFacade();
			ProjectInfo vo = new ProjectInfo();
			int num = ServiceBean.getInstance().getProjectInfoFacade()
					.getProjectInfoCount(vo) + 1;
			BeanUtils.copyProperties(vo, form);
			// vo.setId(num);
			String proNo=form.getProjectNo();
			System.out.println(proNo);
			vo.setProjectNo(proNo);
			vo.setCompanyId(companyId);
			vo.setChannelId(photoUrl+name);
			if("".equals(name)||name==""||name==null){
				vo.setChannelId("");
			}
			vo.setProjectName(form.getProjectName());
			vo.setAddTime(new Date());
			vo.setHeartS(proNo);
			vo.setAdTitle(form.getAdTitle());
			vo.setAdDetail(form.getAdDetail());
			facade.insertProjectInfo(vo);
			
			List<DataMap> getProjectInfo = ServiceBean.getInstance().getProjectInfoFacade().getProjectInfo(vo);
			if(getProjectInfo.size()>0){
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<ads>");
		
		    for(int i=0;i<getProjectInfo.size();i++){
		    	sb.append("<advertising>");
		    	sb.append("<customerName>");
		    	sb.append(getProjectInfo.get(i).get("project_no")+"");
		    	sb.append("</customerName>");
		    	sb.append("<advertisingUrl>");
		    	sb.append(getProjectInfo.get(i).get("project_name")+"");
		    	sb.append("</advertisingUrl>");
		    	sb.append("<iconUrl>");
		    	sb.append(getProjectInfo.get(i).get("channel_id")+"");
		    	sb.append("</iconUrl>");
		    	sb.append("<lanugage>");
		    	sb.append(getProjectInfo.get(i).get("company_id")+"");
		    	sb.append("</lanugage>");
		    	sb.append("<adTitle>");
		    	sb.append(getProjectInfo.get(i).get("adTitle")+"");
		    	sb.append("</adTitle>");
		    	sb.append("<adDetail>");
		    	sb.append(getProjectInfo.get(i).get("adDetail")+"");
		    	sb.append("</adDetail>");
		    	  sb.append("</advertising>");
				}
		  
		    sb.append("</ads>");
		 
			}
			Constant.deleteFile(xmlpath+xmlfileName);
			Constant.createFileContent(xmlpath, xmlfileName,sb.toString().getBytes("UTF-8"));
			
			result.setBackPage(HttpTools.httpServletPath(request, // ����ɹ�����ת��ԭ��ҳ��
					"queryProjectInfoXml"));
			result.setResultCode("inserts"); // ���ò���Code
			result.setResultType("success"); // ���ò���ɹ�
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request, "initInsertxml"));
			if (e instanceof SystemException) { /* ����֪�쳣���н��� */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* ��δ֪�쳣���н�������ȫ�������δ֪�쳣 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}
	
	public ActionForward insertProjectInfoxmlOther(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		String companyId = request.getParameter("companyId");
		String channelId = request.getParameter("channelId");
		System.out.println(channelId);
		Result result = new Result();
		try {
			LoginUser loginUser = (LoginUser) request.getSession()
					.getAttribute(Config.SystemConfig.LOGINUSER);
			if (loginUser == null) {
				return null;
			}
			
			StringBuffer sb = new StringBuffer();
			String userName = loginUser.getUserName();
			ProjectInfoForm form = (ProjectInfoForm) actionForm;
			String name = "";
			String fileFormat = "";
			Hashtable<?, ?> files = form.getMultipartRequestHandler().getFileElements();//获取所有文件路径的枚举；
			if (files != null & files.size() > 0) {
				Enumeration<?> enums = files.keys();
				String fileKey = null;					
				while (enums.hasMoreElements()) {
					fileKey = (String) (enums.nextElement());
					FormFile file = (FormFile) files.get(fileKey);
					if(!file.getFileName().isEmpty())
					{
						fileFormat = file.toString().substring(file.toString().lastIndexOf("."),file.toString().length());
						name=Long.toString(new Date().getTime())+fileFormat;
						//CommUtils.createDateFile(dir); //创建当前文件夹，存在则返回文件名；
						InputStream in = file.getInputStream();
						//photoPath =  photoPath  + name;   //输出文件路径
						System.out.println( photoPath  + name);
						File f = new File( photoPath  + name);
						if(f.exists()){
							f.delete();	
						}
						
						OutputStream out = new FileOutputStream( photoPath  + name);
						out.write(file.getFileData(), 0, file.getFileSize());
							
						out.close();
						out = null;				
						in.close();		
					}
					
				}
		
			
			}
			
			ProjectInfoFacade facade = ServiceBean.getInstance()
					.getProjectInfoFacade();
			ProjectInfo vo = new ProjectInfo();
			int num = ServiceBean.getInstance().getProjectInfoFacade()
					.getProjectInfoCount(vo) + 1;
			BeanUtils.copyProperties(vo, form);
			// vo.setId(num);
			vo.setProjectNo(userName);
			vo.setCompanyId(companyId);
			vo.setChannelId(photoUrl+name);
			if("".equals(name)||name==""||name==null){
				vo.setChannelId("");
			}
			vo.setProjectName(form.getProjectName());
			vo.setAddTime(new Date());
			vo.setHeartS(userName);
			vo.setAdTitle(form.getAdTitle());
			vo.setAdDetail(form.getAdDetail());
			facade.insertProjectInfo(vo);
			
			List<DataMap> getProjectInfo = ServiceBean.getInstance().getProjectInfoFacade().getProjectInfo(vo);
			if(getProjectInfo.size()>0){
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<ads>");
		    for(int i=0;i<getProjectInfo.size();i++){
		    	sb.append("<advertising>");
		    	sb.append("<customerName>");
		    	sb.append(getProjectInfo.get(i).get("project_no")+"");
		    	sb.append("</customerName>");
		    	sb.append("<advertisingUrl>");
		    	sb.append(getProjectInfo.get(i).get("project_name")+"");
		    	sb.append("</advertisingUrl>");
		    	sb.append("<iconUrl>");
		    	sb.append(getProjectInfo.get(i).get("channel_id")+"");
		    	sb.append("</iconUrl>");
		    	sb.append("<lanugage>");
		    	sb.append(getProjectInfo.get(i).get("company_id")+"");
		    	sb.append("</lanugage>");
		    	sb.append("<adTitle>");
		    	sb.append(getProjectInfo.get(i).get("adTitle")+"");
		    	sb.append("</adTitle>");
		    	sb.append("<adDetail>");
		    	sb.append(getProjectInfo.get(i).get("adDetail")+"");
		    	sb.append("</adDetail>");
		    	  sb.append("</advertising>");
				}
		    sb.append("</ads>");
			}
			Constant.deleteFile(xmlpath+xmlfileName);
			Constant.createFileContent(xmlpath, xmlfileName,sb.toString().getBytes("UTF-8"));
			
			result.setBackPage(HttpTools.httpServletPath(request, // ����ɹ�����ת��ԭ��ҳ��
					"queryProjectInfoXml"));
			result.setResultCode("inserts"); // ���ò���Code
			result.setResultType("success"); // ���ò���ɹ�
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request, "initInsertxml"));
			if (e instanceof SystemException) { /* ����֪�쳣���н��� */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* ��δ֪�쳣���н�������ȫ�������δ֪�쳣 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}

	public ActionForward getProjectByCompanyId(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		String companyId = request.getParameter("companyId");

		try {
			ProjectInfo vo = new ProjectInfo();
			if (companyId != null && !"".equals(companyId)) {
				vo.setCondition("company_id = '" + companyId + "'");
			}

			List<DataMap> list = ServiceBean.getInstance()
					.getProjectInfoFacade().getProjectInfo(vo);
			StringBuffer sb = new StringBuffer();
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					sb.append(list.get(i).getAt("id"));
					sb.append(",");
					sb.append(list.get(i).getAt("project_name"));
					sb.append("&");
				}
			}
			response.getWriter().write(sb.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ActionForward queryProjectName(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		String projectName = request.getParameter("projectName");

		try {
			ProjectInfo vo = new ProjectInfo();
			if (projectName != null && !"".equals(projectName)) {
				vo.setCondition("project_name = '" + projectName + "'");
			}

			List<DataMap> list = ServiceBean.getInstance()
					.getProjectInfoFacade().getProjectName(vo);
			StringBuffer sb = new StringBuffer();
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					sb.append(list.get(i).getAt("project_name"));
				}
			}
			response.getWriter().write(sb.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ActionForward initUpdate(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String id = request.getParameter("id");
		ProjectInfo vo = new ProjectInfo();
		vo.setCondition("id='" + id + "'");
		List<DataMap> list = ServiceBean.getInstance().getProjectInfoFacade()
				.getProjectInfo(vo);
		if (list == null || list.size() == 0) {
			Result result = new Result();
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryProjectInfo"));
			result.setResultCode("rowDel");
			result.setResultType("success");
			return mapping.findForward("result");
		}
		request.setAttribute("projectInfo", list.get(0));

		return mapping.findForward("updateProjectInfo");
	}

	public ActionForward initUpdatexml(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LoginUser loginUser = (LoginUser) request.getSession()
				.getAttribute(Config.SystemConfig.LOGINUSER);
		if (loginUser == null) {
			return null;
		}
		
		String userName = loginUser.getUserName();
		
		String id = request.getParameter("id");
		ProjectInfo vo = new ProjectInfo();
		vo.setCondition("id='" + id + "'");
		List<DataMap> list = ServiceBean.getInstance().getProjectInfoFacade()
				.getProjectInfo(vo);
		if (list == null || list.size() == 0) {
			Result result = new Result();
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryProjectInfoXml"));
			result.setResultCode("rowDel");
			result.setResultType("success");
			return mapping.findForward("result");
		}
		request.setAttribute("projectInfo", list.get(0));
         if("admin".equals(userName)){
        	 return mapping.findForward("updateProjectInfoxml");
         }else{
        	 return mapping.findForward("updateProjectInfoxmlOther");
         }
	}

	public ActionForward initUpdateSwitch(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String id = request.getParameter("id");
		ProjectInfo vo = new ProjectInfo();
		vo.setCondition("id='" + id + "'");
		List<DataMap> list = ServiceBean.getInstance().getProjectInfoFacade()
				.getProjectLocationSwitchInfo(vo);
		if (list == null || list.size() == 0) {
			Result result = new Result();
			result.setBackPage(HttpTools.httpServletPath(request,
					"querySwitchInfo"));
			result.setResultCode("rowDel");
			result.setResultType("success");
			return mapping.findForward("result");
		}
		request.setAttribute("projectInfo", list.get(0));

		return mapping.findForward("updateProjectSwitchInfo");
	}

	public ActionForward updateProjectInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		Result result = new Result();
		try {
			ProjectInfoForm form = (ProjectInfoForm) actionForm;

			ProjectInfo vo = new ProjectInfo();
			vo.setCondition("id='" + form.getId() + "'");
			BeanUtils.copyProperties(vo, form);
			ServiceBean.getInstance().getProjectInfoFacade()
					.updatePorjectInfo(vo);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryProjectInfo"));
			result.setResultCode("updates");
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryProjectInfo"));
			if (e instanceof SystemException) { /* ����֪�쳣���н��� */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* ��δ֪�쳣���н�������ȫ�������δ֪�쳣 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}

	public ActionForward updateProjectInfoxml(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		Result result = new Result();
		try {
			String id = request.getParameter("id");
			String project_no = request.getParameter("project_no");
			String project_name = request.getParameter("project_name");
			String channel_id = request.getParameter("channel_id");
			String company_id = request.getParameter("company_id");
			String adTitle = request.getParameter("adTitle");
			String adDetail = request.getParameter("adDetail");

			// ProjectInfoForm form = (ProjectInfoForm) actionForm;
			
			
			ProjectInfoForm form = (ProjectInfoForm) actionForm;
			String name = "";
			String fileFormat = "";
			Hashtable<?, ?> files = form.getMultipartRequestHandler().getFileElements();//获取所有文件路径的枚举；
			if (files != null & files.size() > 0) {
				Enumeration<?> enums = files.keys();
				String fileKey = null;					
				while (enums.hasMoreElements()) {
					fileKey = (String) (enums.nextElement());
					FormFile file = (FormFile) files.get(fileKey);
					if(!file.getFileName().isEmpty())
					{
						fileFormat = file.toString().substring(file.toString().lastIndexOf("."),file.toString().length());
						name=Long.toString(new Date().getTime())+fileFormat;
						//CommUtils.createDateFile(dir); //创建当前文件夹，存在则返回文件名；
						InputStream in = file.getInputStream();
					//	photoPath =  photoPath  + name;   //输出文件路径
						System.out.println( photoPath  + name);
						File f = new File( photoPath  + name);
						if(f.exists()){
							f.delete();	
						}
						
						OutputStream out = new FileOutputStream( photoPath  + name);
						out.write(file.getFileData(), 0, file.getFileSize());
							
						out.close();
						out = null;				
						in.close();		
					}
					
				}
		
			
			}
			

			ProjectInfo vo = new ProjectInfo();
			vo.setCondition("id='" + id + "'");
			vo.setProjectName(project_name);
			vo.setProjectNo(project_no);
			vo.setChannelId(photoUrl+name);
			if("".equals(name)||name==""||name==null){
				vo.setChannelId("");
			}
			vo.setCompanyId(company_id);
			vo.setAdDetail(adDetail);
			vo.setAdTitle(adTitle);
			// BeanUtils.copyProperties(vo, form);
			ServiceBean.getInstance().getProjectInfoFacade()
					.updatePorjectInfo(vo);
			StringBuffer sb=new StringBuffer();
			ProjectInfo voo = new ProjectInfo();
			List<DataMap> getProjectInfo = ServiceBean.getInstance().getProjectInfoFacade().getProjectInfo(voo);
			if(getProjectInfo.size()>0){
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<ads>");
		    for(int i=0;i<getProjectInfo.size();i++){
		    	sb.append("<advertising>");
		    	sb.append("<customerName>");
		    	sb.append(getProjectInfo.get(i).get("project_no")+"");
		    	sb.append("</customerName>");
		    	sb.append("<advertisingUrl>");
		    	sb.append(getProjectInfo.get(i).get("project_name")+"");
		    	sb.append("</advertisingUrl>");
		    	sb.append("<iconUrl>");
		    	sb.append(getProjectInfo.get(i).get("channel_id")+"");
		    	sb.append("</iconUrl>");
		    	sb.append("<lanugage>");
		    	sb.append(getProjectInfo.get(i).get("company_id")+"");
		    	sb.append("</lanugage>");
		    	sb.append("<adTitle>");
		    	sb.append(getProjectInfo.get(i).get("adTitle")+"");
		    	sb.append("</adTitle>");
		    	sb.append("<adDetail>");
		    	sb.append(getProjectInfo.get(i).get("adDetail")+"");
		    	sb.append("</adDetail>");  
		    	sb.append("</advertising>");
				}
		    sb.append("</ads>");
			}
			Constant.deleteFile(xmlpath+xmlfileName);
			Constant.createFileContent(xmlpath, xmlfileName,sb.toString().getBytes("UTF-8"));
			
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryProjectInfoXml"));
			result.setResultCode("updates");
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryProjectInfoXml"));
			if (e instanceof SystemException) { /* ����֪�쳣���н��� */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* ��δ֪�쳣���н�������ȫ�������δ֪�쳣 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}
	
	
	public ActionForward updateProjectInfoxmlOther(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		Result result = new Result();
		try {
			String id = request.getParameter("id");
			//String project_no = request.getParameter("project_no");
			String project_name = request.getParameter("project_name");
			String channel_id = request.getParameter("channel_id");
			String company_id = request.getParameter("company_id");
			String adTitle = request.getParameter("adTitle");
			String adDetail = request.getParameter("adDetail");

			// ProjectInfoForm form = (ProjectInfoForm) actionForm;
			
			
			ProjectInfoForm form = (ProjectInfoForm) actionForm;
			String name = "";
			String fileFormat = "";
			Hashtable<?, ?> files = form.getMultipartRequestHandler().getFileElements();//获取所有文件路径的枚举；
			if (files != null & files.size() > 0) {
				Enumeration<?> enums = files.keys();
				String fileKey = null;					
				while (enums.hasMoreElements()) {
					fileKey = (String) (enums.nextElement());
					FormFile file = (FormFile) files.get(fileKey);
					if(!file.getFileName().isEmpty())
					{
						fileFormat = file.toString().substring(file.toString().lastIndexOf("."),file.toString().length());
						name=Long.toString(new Date().getTime())+fileFormat;
						//CommUtils.createDateFile(dir); //创建当前文件夹，存在则返回文件名；
						InputStream in = file.getInputStream();
						//photoPath =  photoPath  + name;   //输出文件路径
						System.out.println( photoPath  + name);
						File f = new File( photoPath  + name);
						if(f.exists()){
							f.delete();	
						}
						
						OutputStream out = new FileOutputStream( photoPath  + name);
						out.write(file.getFileData(), 0, file.getFileSize());
							
						out.close();
						out = null;				
						in.close();		
					}
					
				}
		
			
			}
			

			ProjectInfo vo = new ProjectInfo();
			vo.setCondition("id='" + id + "'");
			vo.setProjectName(project_name);
			//vo.setProjectNo(project_no);
			vo.setChannelId(photoUrl+name);
			if("".equals(name)||name==""||name==null){
				vo.setChannelId("");
			}
			vo.setCompanyId(company_id);
			vo.setAdDetail(adDetail);
			vo.setAdTitle(adTitle);
			// BeanUtils.copyProperties(vo, form);
			ServiceBean.getInstance().getProjectInfoFacade()
					.updatePorjectInfo(vo);
			StringBuffer sb=new StringBuffer();
			ProjectInfo voo = new ProjectInfo();
			List<DataMap> getProjectInfo = ServiceBean.getInstance().getProjectInfoFacade().getProjectInfo(voo);
			if(getProjectInfo.size()>0){
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<ads>");
		    for(int i=0;i<getProjectInfo.size();i++){
		    	sb.append("<advertising>");
		    	sb.append("<customerName>");
		    	sb.append(getProjectInfo.get(i).get("project_no")+"");
		    	sb.append("</customerName>");
		    	sb.append("<advertisingUrl>");
		    	sb.append(getProjectInfo.get(i).get("project_name")+"");
		    	sb.append("</advertisingUrl>");
		    	sb.append("<iconUrl>");
		    	sb.append(getProjectInfo.get(i).get("channel_id")+"");
		    	sb.append("</iconUrl>");
		    	sb.append("<lanugage>");
		    	sb.append(getProjectInfo.get(i).get("company_id")+"");
		    	sb.append("</lanugage>");
		    	sb.append("<adTitle>");
		    	sb.append(getProjectInfo.get(i).get("adTitle")+"");
		    	sb.append("</adTitle>");
		    	sb.append("<adDetail>");
		    	sb.append(getProjectInfo.get(i).get("adDetail")+"");
		    	sb.append("</adDetail>");  
		    	sb.append("</advertising>");
				}
		    sb.append("</ads>");
			}
			Constant.deleteFile(xmlpath+xmlfileName);
			Constant.createFileContent(xmlpath, xmlfileName,sb.toString().getBytes("UTF-8"));
			
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryProjectInfoXml"));
			result.setResultCode("updates");
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryProjectInfoXml"));
			if (e instanceof SystemException) { /* ����֪�쳣���н��� */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* ��δ֪�쳣���н�������ȫ�������δ֪�쳣 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}

	public ActionForward updateProjectSwitchInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		Result result = new Result();
		try {
			ProjectInfoForm form = (ProjectInfoForm) actionForm;

			ProjectInfo vo = new ProjectInfo();
			String pN = request.getParameter("projectName");
			String s = request.getParameter("status");
			String r = request.getParameter("remark");
			vo.setProjectName(pN);
			vo.setLocationSwitch(s);
			vo.setBeifen(r);
			vo.setCondition("id='" + form.getId() + "'");
			// BeanUtils.copyProperties(vo, form);
			ServiceBean.getInstance().getProjectInfoFacade()
					.updatePorjectSwitchInfo(vo);
			result.setBackPage(HttpTools.httpServletPath(request,
					"querySwitchInfo"));
			result.setResultCode("updates");
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"querySwitchInfo"));
			if (e instanceof SystemException) { /* ����֪�쳣���н��� */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* ��δ֪�쳣���н�������ȫ�������δ֪�쳣 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}

	public ActionForward projectRoleFuncInfo(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String projectId = request.getParameter("projectId");
		String factoryCode = request.getParameter("factoryCode");
		request.setAttribute("trees", ServiceBean.getInstance()
				.getProjectInfoFacade().getProjectRoleInfo(projectId));
		// String foleCodeP=request.getParameter("projectId");
		request.setAttribute("roleCodeP", request.getParameter("projectId")); // ��Ӧ���������
		request.setAttribute("factoryCode", request.getParameter("factoryCode"));
		return mapping.findForward("projectRoleFuncInfo");
	}

	public ActionForward insertProjectRoleFuncInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		try {

			String roleCode = request.getParameter("roleCodeP");
			String funcs = request.getParameter("funcs");
			String factoryCode = request.getParameter("factoryCode");
			// ServiceBean.getInstance().getRoleInfoFacade().insertRoleFuncInfo(roleCode,funcs);
			ServiceBean.getInstance().getProjectInfoFacade()
					.insertRoleProjectFuncInfo(roleCode, funcs);

			response.getWriter().println("1");
			return null;
			/*
			 * RoleInfoForm form = (RoleInfoForm)actionForm;
			 * validateInsert(form); RoleInfo vo = new RoleInfo();
			 * BeanUtils.copyProperties(vo,form);
			 * getRoleInfoFacade().insertRoleInfo(vo);
			 * result.setBackPage(HttpTools
			 * .httpServletPath(request,"queryRoleInfo"));
			 * result.setResultCode("inserts"); result.setResultType("success");
			 */
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryProjectInfo"));
			if (e instanceof SystemException) {/* ����֪�쳣���н��� */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else {/* ��δ֪�쳣���н�������ȫ�������δ֪�쳣 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}

	public ActionForward querySwitchInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String href = request.getServletPath();
		Date start = new Date();
		Result result = new Result();// ���
		PagePys pys = new PagePys();// ҳ������
		DataList list = null; // ����ҳ��List ��logic itrate��ȡ��
		StringBuffer sb = new StringBuffer();// �����ַ�����
		ProjectInfoFacade info = ServiceBean.getInstance()
				.getProjectInfoFacade();// ����userApp������ȡ��user�ֵ䣩
		ProjectInfo pro = new ProjectInfo();

		try {

			ProjectInfoForm form = (ProjectInfoForm) actionForm;
			ProjectInfo vo = new ProjectInfo();
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			String companyId = request.getParameter("companyId");
			String userId = request.getParameter("userId");
			String projectId = request.getParameter("projectName");

			// ���û������ֶ�
			form.setOrderBy("id");
			form.setSort("1");
			// sb.append("1=1");

			/*
			 * if(!projectInfoId.equals("0")){ sb.append(" and p.id in(" +
			 * projectInfoId + ")"); }else{ if(!"0".equals(companyId) &&
			 * companyId != null){ sb.append(" and p.company_id in(" +
			 * companyInfoId + ")"); } }
			 */
			if (startTime != null && !"".equals(startTime)) {
				sb.append(" and substring(p.add_time,1,10) >= '" + startTime
						+ "'");
			}
			if (endTime != null && !"".equals(endTime)) {
				sb.append(" and substring(p.add_time,1,10) <= '" + endTime
						+ "'");
			}
			if (companyId != null && !"".equals(companyId)) {
				sb.append(" and p.company_id='" + companyId + "'");
			}
			if (projectId != null && !"".equals(projectId)) {
				sb.append(" and p.id ='" + projectId + "'");
			}
			if (userId != null && !"".equals(userId)) {
				sb.append(" and p.company_id='" + userId + "'");
				pro.setCondition("company_id = '" + userId + "'");
			}
			List<DataMap> pros = ServiceBean.getInstance()
					.getProjectInfoFacade().getProjectLocationSwitchInfo(vo);
			request.setAttribute("project", pros);

			request.setAttribute("fNow_date", startTime);
			request.setAttribute("now_date", endTime);
			request.setAttribute("companyId", companyId);
			request.setAttribute("userId", userId);
			request.setAttribute("projectId", projectId);

			vo.setCondition(sb.toString());

			BeanUtils.copyProperties(vo, form);
			list = info.getProjectLocationSwitchInfoBvo(vo);
			BeanUtils.copyProperties(pys, form);
			pys.setCounts(list.getTotalSize());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(request.getQueryString() + "  " + e);
			result.setBackPage(Config.ABOUT_PAGE); /*
													 * ����Ϊ����ҳ�棬���Գ������ת��ϵ
													 * ͳĬ��ҳ��
													 */
			if (e instanceof SystemException) { /* ����֪�쳣���н��� */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* ��δ֪�쳣���н�������ȫ�������δ֪�쳣 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
			request.setAttribute("pageList", list);
			request.setAttribute("PagePys", pys);
		}
		CommUtils.getIntervalTime(start, new Date(), href);
		return mapping.findForward("querySwitchInfo");
	}

	public ActionForward deletexml(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {

		Result result = new Result();
		try {
			ProjectInfoForm form = (ProjectInfoForm) actionForm;

			ProjectInfo vo = new ProjectInfo();
			vo.setCondition("id='" + form.getId() + "'");
			BeanUtils.copyProperties(vo, form);
			ServiceBean.getInstance().getProjectInfoFacade()
					.deletePorjectInfoxml(vo);
			ProjectInfo voo = new ProjectInfo();
			StringBuffer sb=new StringBuffer();
			List<DataMap> getProjectInfo = ServiceBean.getInstance().getProjectInfoFacade().getProjectInfo(voo);
			if(getProjectInfo.size()>0){
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<ads>");
		    for(int i=0;i<getProjectInfo.size();i++){
		    	sb.append("<advertising>");
		    	sb.append("<customerName>");
		    	sb.append(getProjectInfo.get(i).get("project_no")+"");
		    	sb.append("</customerName>");
		    	sb.append("<advertisingUrl>");
		    	sb.append(getProjectInfo.get(i).get("project_name")+"");
		    	sb.append("</advertisingUrl>");
		    	sb.append("<iconUrl>");
		    	sb.append(getProjectInfo.get(i).get("channel_id")+"");
		    	sb.append("</iconUrl>");
		    	sb.append("<lanugage>");
		    	sb.append(getProjectInfo.get(i).get("company_id")+"");
		    	sb.append("</lanugage>");
		    	sb.append("<adTitle>");
		    	sb.append(getProjectInfo.get(i).get("adTitle")+"");
		    	sb.append("</adTitle>");
		    	sb.append("<adDetail>");
		    	sb.append(getProjectInfo.get(i).get("adDetail")+"");
		    	sb.append("</adDetail>");  
		    	sb.append("</advertising>");
				}
		    sb.append("</ads>");
			}
			Constant.deleteFile(xmlpath+xmlfileName);
			Constant.createFileContent(xmlpath, xmlfileName,sb.toString().getBytes("UTF-8"));
			
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryProjectInfoXml"));
			result.setResultCode("deletes");
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryProjectInfoXml"));
			if (e instanceof SystemException) { /* ����֪�쳣���н��� */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* ��δ֪�쳣���н�������ȫ�������δ֪�쳣 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
		}
		return mapping.findForward("result");
	}
	public String getServerName() throws Exception {
		String serverName = "";
		Properties pros = new Properties();
		pros.load(this.getClass().getClassLoader().getResourceAsStream("server.properties"));
		serverName = pros.getProperty("servername");
		return serverName;
	}
	public static void main(String[] args) {
		String fileName = "advertising.xml";
		String path="E:/resin/resin-pro-4.0.53/webapps/Custom/WIITE/C7/ads/";
		Constant.deleteFile(path+fileName);
		try {
			Constant.createFileContent(path, fileName,"123".getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public static void createSon() {
//        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
//        dbf.setIgnoringElementContentWhitespace(false);
//        
//        try{
//        
//            DocumentBuilder db=dbf.newDocumentBuilder();
//            Document xmldoc=db.parse(xmlPath);
//        
//            Element root = xmldoc.getDocumentElement();
//            
//            //删除指定节点
//            
//            Element son =xmldoc.createElement("son");
//            son.setAttribute("id", "004");
//            
//            Element name = xmldoc.createElement("name");
//            name.setTextContent("小儿子");
//            son.appendChild(name);
//
//            Element age = xmldoc.createElement("name");
//            age.setTextContent("0");
//            son.appendChild(age);
//            
//            root.appendChild(son);
//            //保存
//            TransformerFactory factory = TransformerFactory.newInstance();
//            Transformer former = factory.newTransformer();
//            former.transform(new DOMSource(xmldoc), new StreamResult(new File(xmlPath)));
//            
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
}
