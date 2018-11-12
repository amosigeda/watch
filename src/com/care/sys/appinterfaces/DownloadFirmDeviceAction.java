package com.care.sys.appinterfaces;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.care.common.config.ServiceBean;
import com.care.common.http.BaseAction;
import com.care.sys.checkinfo.domain.CheckInfo;
import com.godoing.rose.lang.DataMap;

public class DownloadFirmDeviceAction extends BaseAction{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		request.setCharacterEncoding("UTF-8");
		String href= request.getServletPath();
		JSONObject json = new JSONObject();
		String belongProject ="";
		String versionName = "";
		String versionCode="";
		String downloadPath="";
		result = 1;
		try {
			ServletInputStream sis = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(sis));
			StringBuffer sb =new StringBuffer();
			String buf="";
			while((buf=br.readLine())!=null){
				sb.append(buf);
			}
			JSONObject object = JSONObject.fromObject(sb.toString());
			belongProject = object.getString("belong_project");
			versionCode = object.getString("version_code");
			versionName = object.getString("version_name");
			
			CheckInfo upo=new CheckInfo();
			upo.setCondition("version_name='"+versionName+"' and belong_project='"+belongProject+"' limit 1");
			List<DataMap> list= ServiceBean.getInstance().getCheckInfoFacade().getFirmDevice(upo);
			if(list.size()>0){
				Integer hasVersionCode= Integer.parseInt((String) list.get(0).getAt("version_code"));
				Integer versionC = Integer.parseInt(versionCode);
				if(hasVersionCode!=null&&hasVersionCode>versionC){   //表示有更新。
					downloadPath =""+list.get(0).getAt("download_path");
					 json.put("download_address", downloadPath);
				}else{
					 json.put("download_address", "");
				}
			}else{
				result = 0;
			}
			
			if(result==1){
				String zipName="care.zip";
				String dir=request.getSession(true).getServletContext().getRealPath("/upload/zip/" + belongProject+"/"+zipName);
				File f = new File(dir);		 
		        if (!f.exists()) {
		           result=0; 
		        }else{
//		        	 FileInputStream fin = new FileInputStream(f);
//				        BufferedInputStream br1 = new BufferedInputStream(fin);
//				        byte[] buf1 = new byte[1024*8];
//				        int len = 0;
//				        boolean isOnLine = true;
//				        response.reset(); 
//				        if (!isOnLine) { 
//				            URL u = new URL("file:///" + dir);
//				            response.setContentType(u.openConnection().getContentType());
//				            response.setHeader("Content-Disposition", "inline; filename=" + zipName);
//				            // 文件名应该编码成UTF-8
//				        } else { // 纯下载方式
//				        	response.setCharacterEncoding("utf-8");
//				            response.setContentType("application/x-msdownload;charset=utf-8");
//				            response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
//				        }
//				        OutputStream out = response.getOutputStream();
//				        while ((len = br1.read(buf1)) > 0){
//				        	out.write(buf1, 0, len);
//				        }
//				        br.close();
//				        out.close();
//				        br1.close();
		        }
			}
			
		} catch (Exception e) {
			result=-1;
			e.printStackTrace();
		}finally{
			json.put("request", href);
			json.put("resultCode", result);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json.toString());
		}
		
		return null;
	}
	
	
}
