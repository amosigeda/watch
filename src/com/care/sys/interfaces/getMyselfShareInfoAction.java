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
import com.care.sys.shareinfo.domain.ShareInfo;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.log.LogFactory;

public class getMyselfShareInfoAction extends BaseAction {

	Log logger = LogFactory.getLog(getMyselfShareInfoAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject json = new JSONObject();
		try {
			String smdid = request.getParameter("smdid");
			String bg = request.getParameter("bg");
      String touserId="";
			JSONArray jsonA=new JSONArray();
			if (smdid != null&& bg!=null) {
				ShareInfo vo=new ShareInfo();
				vo.setCondition("device_imei='" + smdid + "' and belong_project='"+bg+"'");
				List<DataMap> list = ServiceBean.getInstance().getShareInfoFacade().getShareInfo(vo);
				if(list.size()>0){
					for(int i=0;i<list.size();i++){
						touserId=list.get(i).get("to_user_id")+"";
						json.accumulate("uid", touserId);
						if(!touserId.equals("")){
                             AppUserInfo aio=new AppUserInfo();							
						aio.setCondition("id='"+touserId+"'");
						List<DataMap> listA = ServiceBean.getInstance().getAppUserInfoFacade().getAppUserInfo(aio);
						if(listA.size()>0){
							json.accumulate("name", listA.get(0).get("nick_name")+"");
							json.accumulate("head", listA.get(0).get("head")+"");
							json.accumulate("phone", listA.get(0).get("user_name")+"");
						}
						}
						jsonA.add(json);
						json.clear();
					}
				}
				
				json.put("shareInfo", jsonA);
				result = Constant.SUCCESS_CODE;
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
