package com.care.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FFmpeg {
	public static boolean amrToMp3(String localPath, String targetFilePath) {  
		  
        try {  
  
            System.out.println("**************  ffmpeg ****************");  
  
            java.lang.Runtime rt = Runtime.getRuntime();  
            String command = "/Users/bruse/Desktop/ffmpeg -i " + localPath + " " + targetFilePath;  
  
            System.out.println("/Users/bruse/Desktop/ffmpeg exec command = " + command);  
  
            Process proc = rt.exec(command);  
            InputStream stderr = proc.getErrorStream();  
            InputStreamReader isr = new InputStreamReader(stderr);  
            BufferedReader br = new BufferedReader(isr);  
            String line = null;  
            StringBuffer sb = new StringBuffer();  
            while ((line = br.readLine()) != null)  
                sb.append(line);  
  
            System.out.println("ffmpeg Process errorInfo: " + sb.toString());  
  
            int exitVal = proc.waitFor();  
            System.out.println("ffmpeg Process exitValue: " + exitVal);  
  
            return true;  
        } catch (Exception e) {  
            System.out.println("ffmpeg exec cmd Exception " + e.toString());  
        }  
        return false;  
        }
	
	  public static void main(String[] args) {  
	        String path1 = "/Users/bruse/Desktop/527732_RA8NRoCNXQcrAKOju7zXXTgXHbHJnt4qXaeSSftbzBaXGnsxT49br2e-H4HMlTp4.amr";  
	        String path2 = "/Users/bruse/Desktop/bb.mp3";  
	        amrToMp3(path1, path2);  
	    }

}
