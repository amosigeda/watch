package com.care.sys.interfaces;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.care.common.config.ServiceBean;
import com.care.common.http.BaseAction;
import com.care.common.lang.Constant;
import com.care.sys.appuserinfo.domain.AppUserInfo;
import com.care.sys.deviceactiveinfo.domain.DeviceActiveInfo;
import com.care.sys.msginfo.domain.MsgInfo;
import com.care.sys.relativecallinfo.domain.RelativeCallInfo;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.log.LogFactory;

public class getPhotoCouseAction extends BaseAction {

	Log logger = LogFactory.getLog(getPhotoCouseAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		try {
			String bg = request.getParameter("belong_project");
      
			JSONArray jsonA=new JSONArray();
			if (bg != null) {
					AppUserInfo ao=new AppUserInfo();
					ao.setCondition("projectId='"+bg+"'and downloadstatus='1' and hideline='1'");
					
					List<DataMap> listA=ServiceBean.getInstance().getAppUserInfoFacade().getProjectImageInfo(ao);
				if(listA.size()>0){
					for(int i=0;i<listA.size();i++){
						json1.put("photo_href", listA.get(i).get("downloadpath1")+"");
                          jsonA.add(i, json1);
					}
					json.put("dizhi", jsonA);
					result=Constant.SUCCESS_CODE;
				}else{
					result = Constant.FAIL_CODE;
				}	
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
		json.put(Constant.RESULTCODE, result);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
		return null;
	}
}
