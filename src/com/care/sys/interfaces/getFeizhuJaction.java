package com.care.sys.interfaces;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
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

public class getFeizhuJaction extends BaseAction {

	Log logger = LogFactory.getLog(getFeizhuJaction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		JSONObject json = new JSONObject();
		try {
			String sn = request.getParameter("did");
			//String bg = request.getParameter("bg");
			System.err.println("aaaaa==="+sn);
             String type=request.getParameter("type");
			JSONArray jsonA=new JSONArray();
			if (sn != null) {
				RelativeCallInfo vo=new RelativeCallInfo();
				if(type.equals("5")){
					vo.setCondition("serie_no='" + sn + "' and belong_project='"+12+"' and relative_type='5'");
				}else{
					vo.setCondition("serie_no='" + sn + "' and belong_project='"+12+"'and relative_type is NULL");
				}
				List<DataMap> list = ServiceBean.getInstance().getRelativeCallInfoFacade().getRelativeCallInfo(vo);
				for(int i=0;i<list.size();i++){
					json.accumulate("name", list.get(i).get("nick_name")+"");
					json.accumulate("re_id", list.get(i).get("id")+"");
					String phone=list.get(i).get("phone_number")+"";
					json.accumulate("phone", phone);
					AppUserInfo aao=new AppUserInfo();
					if(phone.equals("")||phone==null){
						phone="00000000000";
					}
					aao.setCondition("user_name='"+phone+"' and belong_project='12'");
					List<DataMap> listAA=ServiceBean.getInstance().getAppUserInfoFacade().getAppUserInfo(aao);
					if(listAA.size()>0){
						json.accumulate("head",listAA.get(0).get("head")+"");
						json.accumulate("id",listAA.get(0).get("id")+"");
					}else{
						json.accumulate("head","");
						json.accumulate("id","");
					}
					json.accumulate("zu", list.get(i).get("zu")+"");
					String userId=list.get(i).get("user_id")+"";
					json.accumulate("user_id",userId);
					
					jsonA.add(json);
					json.clear();
				}
				json.put("guanXiInfo", jsonA);
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
