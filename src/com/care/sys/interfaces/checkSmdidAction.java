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

public class checkSmdidAction extends BaseAction {

	Log logger = LogFactory.getLog(checkSmdidAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject json = new JSONObject();
		try {
			String deviceId = request.getParameter("smdid");

			if (deviceId != null) {
				PhoneInfo po=new PhoneInfo();
				po.setCondition("serie_no='" + deviceId
						+ "' and belong_project='12'");
				List<DataMap> listPo=ServiceBean.getInstance().getPhoneInfoFacade().getPhoneInfo(po);
				if(listPo.size()>0){
					DeviceActiveInfo vo = new DeviceActiveInfo();
					vo.setCondition("device_imei='" + deviceId
							+ "' and belong_project='12' and device_disable='1'");
					List<DataMap> list = ServiceBean.getInstance().getDeviceActiveInfoFacade().getDeviceActiveInfo(vo);
					if (list.size() > 0) {
						result = Constant.SUCCESS_CODE;//1说明被绑定
					} else {
						result = Constant.FAIL_CODE;//0没有被绑定
					}
				}else{
					result = -2;//-2没有录入
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
