package com.project.model;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.UpYun;
import com.upyun.UpException;

public class UploadImageUpyun {
	
	public static String testWriteFile(String fileAdress, String imageType) throws IOException, UpException {
    	
    	// 运行前先设置好以下三个参数
        String BUCKET_NAME = "xyt-upyun";
        String OPERATOR_NAME = "xinyitie";
        String OPERATOR_PWD = "RxRcSp8Jg1CZptLv9pi607Wjs4fkuRnA";
        String[] strNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().split("-");
        int Y = Integer.parseInt(strNow[0]);
        int M = Integer.parseInt(strNow[1]);
        int D = Integer.parseInt(strNow[2]);
    	String savePath = "/"+ Y +"/"+ M+"-"+D +"/"+ UUID.randomUUID().toString().replace("-", "") +"."+imageType;
        File file = new File(fileAdress);
        UpYun upyun = new UpYun(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
        		
        Boolean result = upyun.writeFile(savePath, file);
        if(result){
        	return "http://xyt-upyun.test.upcdn.net/" + savePath;
        }else{
        	return "";
        }
    }


}
