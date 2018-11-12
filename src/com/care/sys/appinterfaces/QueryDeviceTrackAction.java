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
import com.care.sys.locationinfo.domain.LocationInfo;
import com.care.sys.locationinfo.domain.logic.LocationInfoFacade;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.log.LogFactory;

public class QueryDeviceTrackAction extends BaseAction {

	
	Log logger = LogFactory.getLog(QueryDeviceTrackAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("UTF-8");
		String href= request.getServletPath();
		Date start = new Date();
		JSONObject json = new JSONObject();
		JSONObject locationData = new JSONObject();
		JSONArray trackData = new JSONArray();
		String user_id = "";
		String belongProject = "";
		try{
			ServletInputStream input = request.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			StringBuffer sb = new StringBuffer();
			String online = "";
			while((online = reader.readLine()) != null){
				sb.append(online);
			}
			JSONObject object = JSONObject.fromObject(sb.toString());
			user_id = object.getString("user_id");
			String device_id = object.getString("device_id");
			String start_time = object.getString("start_time");
			String end_time = object.getString("end_time");
			belongProject = object.getString("belong_project");
			
			LocationInfo vo = new LocationInfo();
			vo.setCondition("serie_no = '"+device_id+"' and belong_project='"+belongProject+"' and upload_time >= '"+start_time+"' and upload_time <= '"+end_time+"' and longitude != '0.000000' and latitude != '90.000000' and s_t = '1'");
			vo.setOrderBy("upload_time");
			json.put("request", href);
			LocationInfoFacade facade = ServiceBean.getInstance().getLocationInfoFacade();
			
			List<DataMap> list = facade.getLocationListInfo(vo);
			if(list.size() > 0){
				int size = list.size();
				for(int i=0;i<size;i++){
//					
					locationData.put("lat", list.get(i).getAt("change_latitude"));
					locationData.put("lng", list.get(i).getAt("change_longitude"));
					locationData.put("no_trans_lat", list.get(i).getAt("latitude"));
					locationData.put("no_trans_lng", list.get(i).getAt("longitude"));
					locationData.put("battery", list.get(i).getAt("battery"));
					locationData.put("type", list.get(i).getAt("location_type"));
					locationData.put("time", list.get(i).getAt("upload_time"));
					locationData.put("e_time", list.get(i).getAt("e_t"));
					locationData.put("accuracy", list.get(i).getAt("accuracy"));
					locationData.put("fall", list.get(i).getAt("fall"));
					
					trackData.add(i,locationData);
				}
				result = Constant.SUCCESS_CODE;
			}else {
				result = Constant.FAIL_CODE;
			}
			getMessageFrom(json, user_id, belongProject);
			
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
		json.put("track", trackData);
		insertVisit(href,belongProject,user_id,0,start,new Date(),json.toString().getBytes("utf-8").length);
//		insertVisitReason(href, belongProject, "轨迹", user_id, 0, start,new Date(),json.toString().getBytes("utf-8").length);
		response.setCharacterEncoding("UTF-8");	
		response.getWriter().write(json.toString());	
		return null;
	}	

}
