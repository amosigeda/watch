package com.care.sys.interfaces;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;

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
import com.care.sys.deviceactiveinfo.domain.DeviceActiveInfo;
import com.care.sys.msginfo.domain.MsgInfo;
import com.care.sys.phoneinfo.domain.PhoneInfo;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.log.LogFactory;

public class checkDeviceStatusAction extends BaseAction {

	Log logger = LogFactory.getLog(checkDeviceStatusAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject json = new JSONObject();
		try {
			String smdid = request.getParameter("smdid");

			if (smdid != null) {
				
					PhoneInfo  vo=new PhoneInfo();
					vo.setCondition("serie_no='" + smdid
							+ "' and belong_project='12'");
					List<DataMap> list = ServiceBean.getInstance().getPhoneInfoFacade().getPhoneInfo(vo);
					if (list.size() > 0) {
						String deviceStatus=list.get(0).get("on_line")+"";
						if(deviceStatus.equals("1")){
							result = Constant.SUCCESS_CODE;
						}else{
							result = Constant.FAIL_CODE;
						}
					} else {
						result = Constant.FAIL_CODE;
					}
			} else {
				result = -3;//-3smdid为空
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
