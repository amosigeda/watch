package com.care.sys.interfaces;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

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
import com.care.sys.relativecallinfo.domain.RelativeCallInfo;
import com.godoing.rose.log.LogFactory;

public class updateImportantPeopleAction extends BaseAction {

	Log logger = LogFactory.getLog(updateImportantPeopleAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("UTF-8");
		JSONObject json = new JSONObject();
		String href = request.getServletPath();
		try {
		/*	ServletInputStream input = request.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			StringBuffer sb = new StringBuffer();
			String online = "";
			while ((online = reader.readLine()) != null) {
				sb.append(online);
			}
			JSONObject object = JSONObject.fromObject(sb.toString());*/
			//String type = object.getString("type");
			
			String type = request.getParameter("type");
			// 1添加2修改3删除
			String smdid = request.getParameter("smdid");
		//	String relative = object.getString("relative");
			
			String belongProject = request.getParameter("b_g");
		
			RelativeCallInfo vo = new RelativeCallInfo();
			if (smdid != null) {
				if (type.equals("1")) {
					String nickName = request.getParameter("name");
					String phone = request.getParameter("phone");
					String userId = request.getParameter("user_id");
					System.err.println("名字"+nickName);
                      vo.setAddTime(new Date());
                      vo.setBelongProject(belongProject);
                      vo.setNickName(nickName);
                      vo.setPhoneNumber(phone);
                      vo.setRelativeType("5");
                      vo.setSerieNo(smdid);
                      vo.setUserId(userId);
                      vo.setStatus("1");
                      ServiceBean.getInstance().getRelativeCallInfoFacade().insertRelativeCallInfo(vo);
				}else if(type.equals("2")){
					String nickName = request.getParameter("name");
					String phone = request.getParameter("phone");
					String userId = request.getParameter("user_id");
					String id=request.getParameter("id");
					vo.setCondition("id='"+id+"'");
					  vo.setAddTime(new Date());
                      vo.setBelongProject(belongProject);
                      vo.setNickName(nickName);
                      vo.setPhoneNumber(phone);
                      vo.setRelativeType("5");
                      vo.setSerieNo(smdid);
                      vo.setStatus("");
                      vo.setUserId(userId);
                      ServiceBean.getInstance().getRelativeCallInfoFacade().updateRelativeCallInfo(vo);
				}else if(type.equals("3")){
					String id=request.getParameter("id");
					vo.setCondition("id='"+id+"'");
					ServiceBean.getInstance().getRelativeCallInfoFacade().deleteRelativeCallInfo(vo);
				}
				result=1;
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
		json.put(Constant.RESULTCODE, result);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
		return null;
	}
}
