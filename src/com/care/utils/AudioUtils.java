package com.care.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AudioUtils {
	/** 
     * ffmpeg.exe文件所在的路径 
     */  
    private final static String FFMPEG_PATH;  
    static {  
        FFMPEG_PATH = AudioUtils.class.getResource("ffmpeg.exe").getFile();  
    }  
    /** 
     * 将一个amr文件转换成mp3文件 
     * @param amrFile  
     * @param mp3File  
     * @throws IOException  
     */  
    public static void amr2mp3(String amrFileName, String mp3FileName) throws IOException {  
        Runtime runtime = Runtime.getRuntime();  
        Process process = runtime.exec(FFMPEG_PATH + " -i "+amrFileName+" -ar 8000 -ac 1 -y -ab 12.4k " + mp3FileName);  
        InputStream in = process.getErrorStream();  
        BufferedReader br = new BufferedReader(new InputStreamReader(in));  
        try {  
            String line = null;  
            while((line = br.readLine())!=null) {  
                System.out.println(line);  
            }  
            if(process.exitValue() != 0 ) {  
                //如果转换失败，这里需要删除这个文件（因为有时转换失败后的文件大小为0）  
                new File(mp3FileName).delete();  
                throw new RuntimeException("转换失败！");  
            }  
        } finally {  
            //为了避免这里抛出的异常会覆盖上面抛出的异常，这里需要用捕获异常。  
            try {  
                in.close();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
    public static void main(String[] args) {
    	try {
    		String path1 = "F:/voice/abc.amr";
    		String path2 = "F:/voice/18735662247dasadsfasda.mp3";
			amr2mp3(path1,path2);
			System.out.println(3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
