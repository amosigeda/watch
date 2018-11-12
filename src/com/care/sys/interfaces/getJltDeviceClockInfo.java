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
import com.care.sys.deviceactiveinfo.domain.DeviceActiveInfo;
import com.care.sys.msginfo.domain.MsgInfo;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.log.LogFactory;

public class getJltDeviceClockInfo extends BaseAction {

	Log logger = LogFactory.getLog(getJltDeviceClockInfo.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject json = new JSONObject();
		Date start = new Date();
		String href=request.getServletPath();
	String user_idd="";
		try {
			String did = request.getParameter("did");
      
			JSONArray jsonA=new JSONArray();
			if (did != null) {
				DeviceActiveInfo vo = new DeviceActiveInfo();
				vo.setCondition("id='" + did + "'");
				List<DataMap> listU = ServiceBean.getInstance()
						.getDeviceActiveInfoFacade().getDeviceActiveInfo(vo);
				if(listU.size()>0){
				String	user_id=listU.get(0).getAt("user_id")+"";
					user_idd=user_id;
				}
				vo.setCondition("did='" + did + "'");
				List<DataMap> list = ServiceBean.getInstance()
						.getDeviceActiveInfoFacade().getDeviceClockInfo(vo);
				if(list.size()>0){
					
				for(int i=0;i<list.size();i++){
					json.accumulate("name", list.get(i).get("name")+"");
					json.accumulate("clock", list.get(i).get("clock")+"");
					json.accumulate("repeatTimes", list.get(i).get("repeatTimes")+"");
					json.accumulate("zu", list.get(i).get("zu")+"");
					jsonA.add(json);
					json.clear();
				}
			}
				json.put("clockInfo", jsonA);
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
		insertVisitReason(href, "12", "闹钟获取", user_idd, 0, start, new Date(),json.toString().getBytes("utf-8").length);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
		return null;
	}
}
