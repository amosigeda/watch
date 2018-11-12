package com.care.sys.interfaces;

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
import com.care.sys.relativecallinfo.domain.RelativeCallInfo;
import com.godoing.rose.lang.DataMap;
import com.godoing.rose.log.LogFactory;

public class updateUserAddressAction extends BaseAction {

	Log logger = LogFactory.getLog(updateUserAddressAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("UTF-8");
		JSONObject json = new JSONObject();
		JSONObject json1 = new JSONObject();
		JSONArray jsonA = new JSONArray();
		String href = request.getServletPath();
		try {
			ServletInputStream input = request.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			StringBuffer sb = new StringBuffer();
			String online = "";
			while ((online = reader.readLine()) != null) {
				sb.append(online);
			}
			JSONObject object = JSONObject.fromObject(sb.toString());
			String type = object.getString("type");
			// 1添加2修改3删除4查找

			String belongProject = object.getString("b_g");

			RelativeCallInfo vo = new RelativeCallInfo();
			if (type != null) {
				if (type.equals("1")) {// 添加
					String smdid = object.getString("smdid");
					String address = request.getParameter("address");
					System.err.println(address);
					vo.setAddTime(new Date());
					vo.setBelongProject(belongProject);
					vo.setAddress(address);
					vo.setSerieNo(smdid);
					ServiceBean.getInstance().getRelativeCallInfoFacade()
							.insertCommonAddress(vo);
				} else if (type.equals("2")) {// 修改
					// String relative = object.getString("relative");
					String address = request.getParameter("address");
					System.err.println(address);
					String id = object.getString("id");
					vo.setCondition("id='" + id + "'");
					vo.setAddTime(new Date());
					if(address.equals("")){
						vo.setAddress("1");
					}else{
						vo.setAddress(address);
					}
					ServiceBean.getInstance().getRelativeCallInfoFacade()
							.updateCommonAddressInfo(vo);
				} else if (type.equals("3")) {
					String id = object.getString("id");
					vo.setCondition("id='" + id + "'");
					ServiceBean.getInstance().getRelativeCallInfoFacade()
							.deleteCommonAddressInfo(vo);
				} else if (type.equals("4")) {
					String smdid = object.getString("smdid");
					vo.setCondition("smdid='" + smdid
							+ "' and belong_project='" + belongProject + "'");
					List<DataMap> list = ServiceBean.getInstance()
							.getRelativeCallInfoFacade()
							.getCommonAddressInfo(vo);
					if (list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							json1.accumulate("address",list.get(i).get("address") + "");
							json1.accumulate("id",list.get(i).get("id") + "");
							jsonA.add(i, json1);
							json1.clear();
						}
						json.put("all", jsonA);
					}else{
						json.put("all", jsonA);
					}
				}
				result = 1;
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
