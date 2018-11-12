package com.care.sys.appinterfaces;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

import javax.servlet.ServletInputStream;
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
import com.godoing.rose.log.LogFactory;

public class XpsSetDeviceActiveAction extends BaseAction {

	Log logger = LogFactory.getLog(XpsSetDeviceActiveAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		request.setCharacterEncoding("UTF-8");
		JSONObject json = new JSONObject();
		// String serieNo = request.getParameter("serie_no");
		// System.err.println("serieNo="+serieNo);
		// String userId = request.getParameter("userId");
		// String bg = request.getParameter("b_g");
		String href = request.getServletPath();
		ServletInputStream input = request.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		StringBuffer sb = new StringBuffer();
		String online = "";

		while ((online = reader.readLine()) != null) {
			sb.append(online);
		}

		changeToMp3(
				"/usr/voice/resin-pro-4.0.51/webapps/GXCareDevice/voice/abc.amr",
				"/usr/voice/resin-pro-4.0.51/webapps/GXCareDevice/voice/abc.mp3");
		
		System.out.println("收到的字符串为=" + sb.toString());
		JSONObject object = JSONObject.fromObject(sb.toString());
		String path1 = object.getString("path1");
		String name = object.getString("name");
		try {
			File oldf = new File(path1);// 源文件路径
			//File newf = new File("/usr/voice/resin-pro-4.0.51/webapps/GXCareDevice/voice/"+ name + ".amr");// 新文件路径（注意要用 \\来代替\，转义字符）
			
			File newf = new File("/usr/voice/resin-pro-4.0.51/webapps/GXCareDevice/voice/"+name+".amr");// 新文件路径（注意要用 \\来代替\，转义字符）
			copyFileUsingFileChannels(oldf, newf);
			//Thread.sleep(2000);
			changeToMp3(
					"/usr/voice/resin-pro-4.0.51/webapps/GXCareDevice/voice/"
							+ name + ".amr",
					"/usr/voice/resin-pro-4.0.51/webapps/GXCareDevice/voice/"+name+".mp3");
			
			changeToMp3(
					"/usr/local/tomcat/apache-tomcat-8.5.27/webapps/yg_service/static/mp/voice/"
							+ name + ".amr",
					"/usr/voice/resin-pro-4.0.51/webapps/GXCareDevice/voice/tom-resin.mp3");
			
			changeToMp3(
					"/usr/local/tomcat/apache-tomcat-8.5.27/webapps/yg_service/static/mp/voice/"
							+ name + ".amr",
					"/usr/local/tomcat/apache-tomcat-8.5.27/webapps/yg_service/static/mp/voice/111.mp3");
			
			changeToMp3(
					"/usr/voice/resin-pro-4.0.51/webapps/GXCareDevice/voice/"
							+ name + ".amr",
					"/usr/voice/resin-pro-4.0.51/webapps/GXCareDevice/voice/"+name+".mp3");
			
			json.put("path2", "http://39.107.108.146:8080/GXCareDevice/voice/"
					+ name + ".mp3");
		
			

			result = Constant.SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			result = Constant.EXCEPTION_CODE;

		}
		json.put("request", href);
		json.put(Constant.RESULTCODE, result);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json.toString());
		return null;
	}

	public static void copyfile(File oldfile, File newfile) throws IOException {
		// 复制文件
		FileInputStream ins = new FileInputStream(oldfile);
		FileOutputStream out = new FileOutputStream(newfile);
		// 自定义缓冲对象
		byte[] b = new byte[1024];
		int n = 0;
		while ((n = ins.read(b)) != -1) {
			out.write(b, 0, b.length);
		}
		ins.close();
		out.close();

		System.out.println("copy success");
	}
	
	private static void copyFileUsingFileChannels(File source, File dest) throws IOException {    
        FileChannel inputChannel = null;    
        FileChannel outputChannel = null;    
    try {
        inputChannel = new FileInputStream(source).getChannel();
        outputChannel = new FileOutputStream(dest).getChannel();
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
    } finally {
        inputChannel.close();
        outputChannel.close();
        System.out.println("copy success");
    }
}

	public static void changeToMp3(String sourcePath, String targetPath) {
		File source = new File(sourcePath);
		File target = new File(targetPath);
		AudioAttributes audio = new AudioAttributes();
		Encoder encoder = new Encoder();

		audio.setCodec("libmp3lame");
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);

		try {
			encoder.encode(source, target, attrs);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InputFormatException e) {
			e.printStackTrace();
		} catch (EncoderException e) {
			e.printStackTrace();
		}
		

	}
	public static void main(String[] args) {
		String path1 = "G:\\voice\\abc.amr";
		String path3 = "F:\\voice\\abcd.amr";
		String path2 = "F:\\voice\\1dfadsfa.mp3";
		String path4 = "G:\\voice\\abc.mp3";
		File oldf = new File(path1);// 源文件路径
		//File newf = new File("/usr/voice/resin-pro-4.0.51/webapps/GXCareDevice/voice/"+ name + ".amr");// 新文件路径（注意要用 \\来代替\，转义字符）
		File newf = new File(path3);
		//File newf = new File("/usr/voice/resin-pro-4.0.51/webapps/GXCareDevice/voice/"+name+".amr");// 新文件路径（注意要用 \\来代替\，转义字符）
		try {
			copyFileUsingFileChannels(oldf, newf);
			changeToMp3(path3,path2);
			
			changeToMp3(path1,path4);
System.out.println(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
