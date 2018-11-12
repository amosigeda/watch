package com.care.sys.appinterfaces;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletInputStream;
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
import com.care.sys.relativecallinfo.domain.RelativeCallInfo;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.log.LogFactory;

public class getUserInformaAtion extends BaseAction{

	Log logger = LogFactory.getLog(getUserInformaAtion.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("UTF-8");
		String href= request.getServletPath();
		Date start = new Date();
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		String serieNo= "";
		String belongProject = "";
		String userIdd="";
		try{ 
			ServletInputStream input = request.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			StringBuffer sb = new StringBuffer();
			String online = "";
			while((online = reader.readLine()) != null){
				sb.append(online);
			}
			JSONObject object = JSONObject.fromObject(sb.toString());
			serieNo = object.getString("serie_no");
			belongProject = object.getString("belong_project");
			
	    	RelativeCallInfo dai=new RelativeCallInfo();
			dai.setCondition("serie_no ='"+serieNo+"' and belong_project='"+belongProject+"'");
			List<DataMap> deviceList = ServiceBean.getInstance().getRelativeCallInfoFacade().getRelativeCallInfo(dai);
			DeviceActiveInfo vo=new DeviceActiveInfo();
			vo.setCondition("device_imei ='"+serieNo+"' and belong_project='"+belongProject+"' and device_disable='1'");
			List<DataMap> info=ServiceBean.getInstance().getDeviceActiveInfoFacade().getDeviceActiveInfo(vo);
			System.out.println(deviceList.size());
			if(deviceList.size() > 0){
					for(int i=0;i<deviceList.size(); i++){
						String s = deviceList.get(i).getAt("phone_number")+"";
						AppUserInfo ao=new AppUserInfo();
						ao.setCondition("user_name='"+s+"' and belong_project='"+belongProject+"'");
					List<DataMap> listAo=ServiceBean.getInstance().getAppUserInfoFacade().getAppUserInfo(ao);
					if(listAo.size()>0){
						json.accumulate("user_id",listAo.get(0).get("id")+"");
						jsonArray.add(json);
						json.clear();
					}	
					}
					if(info.size()>0){
						userIdd=info.get(0).get("user_id")+"";
						json1.accumulate("user_id",  info.get(0).get("user_id")+"");
						jsonArray.add(json1);
					}
			}else{
				if(info.size()>0){
					userIdd=info.get(0).get("user_id")+"";
					json1.accumulate("user_id",  info.get(0).get("user_id")+"");
					jsonArray.add(json1);
				}
			}
			
			json.put("data", jsonArray);
			result = Constant.SUCCESS_CODE;
			
			
		}catch(Exception e){
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
		//insertVisit(href,belongProject,serieNo,0,start,new Date(),json.toString().getBytes("utf-8").length);
		insertVisitReason(href, belongProject, "获取所有绑定历史用户", userIdd, 0, start, new Date(), json.toString().getBytes("utf-8").length);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());	
		return null;
	}
	
}
