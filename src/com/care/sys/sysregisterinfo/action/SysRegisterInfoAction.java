package com.care.sys.sysregisterinfo.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.care.common.config.Config;
import com.care.common.config.ServiceBean;
import com.care.common.http.BaseAction;
import com.care.common.lang.CommUtils;
import com.care.common.lang.Constant;
import com.care.sys.companyinfo.domain.CompanyInfo;
import com.care.sys.companyinfo.form.CompanyInfoForm;
import com.care.sys.roleinfo.domain.RoleInfo;
import com.care.sys.sysregisterinfo.domain.UserInfo;
import com.care.sys.sysregisterinfo.domain.logic.SysRegisterInfoFacade;
import com.care.sys.sysregisterinfo.form.UserInfoForm;
import com.godoing.rose.http.common.HttpTools;
import com.godoing.rose.http.common.PagePys;
import com.godoing.rose.http.common.Result;
import com.godoing.rose.lang.DataList;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.lang.MD5;
import com.godoing.rose.lang.SystemException;
import com.godoing.rose.log.LogFactory;

public class SysRegisterInfoAction extends BaseAction {
	Log logger = LogFactory.getLog(SysRegisterInfoAction.class);

	public ActionForward queryRegisterInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		int roleId = 0;
		int applyStatus = 3;
		String href = request.getServletPath();
		Date start = new Date();
		Result result = new Result(); // 结果集
		PagePys pys = new PagePys(); // 页面属性
		DataList list = null; // 返回页面List 用logic itrate获取。
		List<DataMap> rlist = null;
		StringBuffer sb = new StringBuffer(); // 创建字符串容器

		rlist = ServiceBean.getInstance().getRoleInfoFacade()
				.getRoleInfo(new RoleInfo());

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String userName = request.getParameter("userName");
		String phoneNo = request.getParameter("phoneNo");
		if (request.getParameter("roleId") != null
				&& !"".equals(request.getParameter("roleId"))) {
			roleId = Integer.valueOf(request.getParameter("roleId"));
		}
		request.setAttribute("roleList", CommUtils.getPrintRegisterSelect(
				rlist, "roleId", "roleName", "id", roleId));
		SysRegisterInfoFacade infoFacade = ServiceBean.getInstance()
				.getRegisterInfoFacade();

		if (request.getParameter("apply_status") != null
				&& !"".equals(request.getParameter("apply_status"))) {
			applyStatus = Integer.valueOf(request.getParameter("apply_status"));
		}
		request.setAttribute("apply_status",
				CommUtils.getPrintApplyStatus(applyStatus));

		try {
			UserInfoForm form = (UserInfoForm) actionForm;
			UserInfo vo = new UserInfo();

			if (startTime != null && !"".equals(startTime)) {
				sb.append("subString(createDate,1,10)>='" + startTime + "'");
			}
			if (endTime != null && !"".equals(endTime)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("subString(createDate,1,10)<='" + endTime + "'");
			}
			if (userName != null && !"".equals(userName)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("userName='" + userName + "'");
			}
			if (phoneNo != null && !"".equals(phoneNo)) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("phoneNo like'%" + phoneNo + "%'");
			}
			if (roleId != 0) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("roleinfo.id=" + roleId);
			}

			if (applyStatus != 3) {
				if (sb.length() > 0) {
					sb.append(" and ");
				}
				sb.append("apply_status=" + applyStatus);
			}

			if (sb.length() > 0) {
				sb.append(" and ");
			}
			sb.append("isInApply = '0' ");

			/* 设置化排序字段 */
			form.setOrderBy("userInfo.createDate");
			form.setSort("1");

			/* 查询所有注册用户信息 */
			UserInfo uf = new UserInfo();
			List<DataMap> users = ServiceBean.getInstance()
					.getRegisterInfoFacade().getRegisterInfo(uf);
			request.setAttribute("fNow_date", startTime);
			request.setAttribute("now_date", endTime);
			request.setAttribute("userName", userName);
			request.setAttribute("phoneNo", phoneNo);

			vo.setCondition(sb.toString());

			vo.setUserName(userName);
			vo.setPhoneNo(phoneNo);
			BeanUtils.copyProperties(vo, form);

			list = infoFacade.getRegisterInfoListByVo(vo);
			BeanUtils.copyProperties(pys, form);
			BeanUtils.copyProperties(pys, form);
			pys.setCounts(list.getTotalSize());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(request.getQueryString() + "  " + e);
			result.setBackPage(Config.ABOUT_PAGE); /* 这里为管理页面，所以出错后跳转到系统默认页面 */
			if (e instanceof SystemException) { /* 对已知异常进行解析 */
				result.setResultCode(((SystemException) e).getErrCode());
				result.setResultType(((SystemException) e).getErrType());
			} else { /* 对未知异常进行解析，并全部定义成未知异常 */
				result.setResultCode("noKnownException");
				result.setResultType("sysRunException");
			}
		} finally {
			request.setAttribute("result", result);
			request.setAttribute("pageList", list);
			request.setAttribute("PagePys", pys);
		}
		CommUtils.getIntervalTime(start, new Date(), href);
		return mapping.findForward("queryRegisterInfo");

	}

	public ActionForward insertRegisterInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		String result = null;
		List<DataMap> list = null;
		try {
			UserInfoForm form = (UserInfoForm) actionForm;
			UserInfo vo = new UserInfo();
			StringBuffer sb = new StringBuffer();
			String userName = request.getParameter("userName");
			String passWord = request.getParameter("passWrd");
			String phoneNo = request.getParameter("phoneNo");
			String applyReason = request.getParameter("applyReason");
			String remark = request.getParameter("remark");

			BeanUtils.copyProperties(form, vo); // 把表单信息复制到用户信息中
			vo.setUserName(userName);
			if (userName != null && !"".equals(userName)) {
				sb.append("userName='" + userName + "'");
			}
			vo.setCondition(sb.toString());
			list = ServiceBean.getInstance().getRegisterInfoFacade()
					.getRegisterInfo(vo);
			if (!list.isEmpty()) {
				response.getWriter().write("false");
				return null;
			}

			vo = new UserInfo();
			vo.setUserName(userName);
			vo.setUserCode(userName);
			vo.setPassWrd(MD5.MD5(passWord));
			vo.setPassWrd1(passWord);
			vo.setCreateDate(new Date());
			vo.setPhoneNo(phoneNo);
			vo.setRemark(remark);
			vo.setTag(0);
			vo.setGroupCode("0");
			vo.setCode("0");
			vo.setApply_status("0");
			vo.setAddUser(userName);
			vo.setIsInApply("0"); // 外部账户，isInApply设置为0
			vo.setApplyReason(applyReason);

			int intNum = ServiceBean.getInstance().getRegisterInfoFacade()
					.insertRegisterInfo(vo);

			if (intNum > 0) {
				result = "success";
			} else {
				result = "fail";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mapping.findForward(result);
	}

	public ActionForward getPhoneNofromUserInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		String phoneNo = request.getParameter("phoneNo");
		UserInfo vo = new UserInfo();
		List<DataMap> list = null;
		if (phoneNo != null && !"".equals(phoneNo)) {
			vo.setCondition("phoneNo ='" + phoneNo + "'");
			try {
				list = ServiceBean.getInstance().getRegisterInfoFacade()
						.getRegisterInfo(vo);
				if (!list.isEmpty()) {
					response.getWriter().write("fail");
				} else {
					response.getWriter().write("success");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public ActionForward getUserNamefromUserInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		String userName = request.getParameter("userName");
		UserInfo vo = new UserInfo();
		List<DataMap> list = null;
		if (userName != null && !"".equals(userName)) {
			vo.setCondition("userName ='" + userName + "'");
			try {
				list = ServiceBean.getInstance().getRegisterInfoFacade()
						.getRegisterInfo(vo);
				if (!list.isEmpty()) {
					response.getWriter().write("fail");
				} else {
					response.getWriter().write("success");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public ActionForward initChangeRoleInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo vo = new UserInfo();
		RoleInfo roleInfo = new RoleInfo();
		List<DataMap> list = null;
		UserInfo userInfo = new UserInfo();
		StringBuffer sb = new StringBuffer();
		int id = Integer.valueOf(request.getParameter("id"));
		if (id > 0) {
			sb.append("id =" + id);
		}
		vo.setCondition(sb.toString());
		List<DataMap> rlist = null;
		try {
			roleInfo.setCondition("id !=1");
			rlist = ServiceBean.getInstance().getRoleInfoFacade()
					.getRoleInfo(roleInfo);
			list = ServiceBean.getInstance().getRegisterInfoFacade()
					.getRegisterInfo(vo);
			userInfo.setId(id);
			userInfo.setUserName((String) list.get(0).getAt("userName"));
			userInfo.setGroupCode((String) list.get(0).getAt("groupCode"));

			request.setAttribute("roleList", CommUtils.getPrintSelect(rlist,
					"groupCode", "roleName", "id", userInfo.getGroupCode(), 1));
		} catch (SystemException e) {
			e.printStackTrace();
		} finally {
			request.setAttribute("userInfo", userInfo);
		}
		return mapping.findForward("changeRoleInfo");
	}

	public ActionForward initChangeTagInfo(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo vo = new UserInfo();
		UserInfo userInfo = new UserInfo();
		List<DataMap> list = null;
		StringBuffer sb = new StringBuffer();
		String groupCode = "";
		int id = Integer.valueOf(request.getParameter("id"));

		if (id > 0) {
			sb.append("id ='" + id + "'");
		}
		vo.setCondition(sb.toString());
		List<DataMap> rlist = null;
		try {

			rlist = ServiceBean.getInstance().getRoleInfoFacade()
					.getRoleInfo(new RoleInfo());
			list = ServiceBean.getInstance().getRegisterInfoFacade()
					.getRegisterInfo(vo);
			userInfo.setId(id);
			userInfo.setUserName((String) list.get(0).getAt("userName"));
			userInfo.setApply_status((String) list.get(0).getAt("apply_status"));
			request.setAttribute("status", CommUtils.getPrintTag(Integer
					.valueOf(userInfo.getApply_status())));
		} catch (SystemException e) {
			e.printStackTrace();
		} finally {
			request.setAttribute("userInfo", userInfo);
		}
		return mapping.findForward("changeTagInfo");
	}

	public ActionForward updateGroupCode(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		UserInfoForm form = (UserInfoForm) actionForm;
		UserInfo vo = new UserInfo();
		int id = 0;
		StringBuffer sb = new StringBuffer();
		String groupCode = request.getParameter("groupCode");
		try {
			id = Integer.valueOf(request.getParameter("id"));
			if (form.getId() > 0) {
				sb.append("id=" + form.getId());
			}
			if (form.getGroupCode() != null && !"".equals(form.getGroupCode())) {
				vo.setGroupCode(form.getGroupCode());
			}
			vo.setCode("admin");
			vo.setCondition(sb.toString());
			vo.setUpdateDate(new Date());

			// BeanUtils.copyProperties(vo,form);
			Integer Num = ServiceBean.getInstance().getRegisterInfoFacade()
					.updateGroupCode(vo);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryRegisterInfo"));
			result.setResultCode("updates");
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryRegisterInfo"));
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

	public ActionForward updateTag(ActionMapping mapping,
			ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) {
		Result result = new Result();
		UserInfoForm form = (UserInfoForm) actionForm;
		UserInfo vo = new UserInfo();
		int tag = 0;
		int id = 0;
		StringBuffer sb = new StringBuffer();
		try {
			id = Integer.valueOf(request.getParameter("id"));
			if (id > 0) {
				sb.append("id=" + id);
			}

			if (request.getParameter("tag") != null
					&& !"".equals(request.getParameter("tag"))) {
				tag = Integer.valueOf(request.getParameter("tag"));
				vo.setTag(tag);
				if (tag == 0) {
					vo.setApply_status("2");
				} else if (tag == 1) {
					vo.setApply_status("1");
				}
			}

			vo.setCondition(sb.toString());
			vo.setUpdateDate(new Date());

			Integer Num = ServiceBean.getInstance().getRegisterInfoFacade()
					.updateRegisterInfo(vo);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryRegisterInfo"));
			result.setResultCode("updates");
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage(HttpTools.httpServletPath(request,
					"queryRegisterInfo"));
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
		return mapping.findForward("queryProjectInfo");
	}

	public ActionForward copyFile(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) {
		Result result = new Result();

		StringBuffer sb = new StringBuffer();
		String url1 = request.getParameter("yuan");
		String url2 = request.getParameter("mubiao");
		try {
			// 创建目标文件夹
			(new File(url2)).mkdirs();
			// 获取源文件夹当前下的文件或目录
			File[] file = (new File(url1)).listFiles();
			for (int i = 0; i < file.length; i++) {
				if (file[i].isFile()) {
					// 复制文件
					copyFile(file[i], new File(url2 + file[i].getName()));
				}
				if (file[i].isDirectory()) {
					// 复制目录
					String sourceDir = url1 + File.separator
							+ file[i].getName();
					String targetDir = url2 + File.separator
							+ file[i].getName();
					copyDirectiory(sourceDir, targetDir);
				}
			}
			result.setResultType("success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(request.getQueryString() + "  " + e);
			result.setBackPage("fail");
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
		return mapping.findForward("successCopyFile");
	}

	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
				copyFile(sourceFile, targetFile);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	// 复制文件
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		// 新建文件输入流并对它进行缓冲
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		// 新建文件输出流并对它进行缓冲
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		// 缓冲数组
		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		// 刷新此缓冲的输出流
		outBuff.flush();

		// 关闭流
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}
}
