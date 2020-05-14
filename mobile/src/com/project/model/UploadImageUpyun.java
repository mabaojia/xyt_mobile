package com.project.model;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.UpYun;
import com.jfinal.kit.PropKit;
import com.upyun.UpException;

public class UploadImageUpyun {
	
	public static String testWriteFile(String fileAdress, String imageType) throws IOException, UpException {
    	
    	// 运行前先设置好以下三个参数
        String BUCKET_NAME = PropKit.get("upyunBuketName");
        String OPERATOR_NAME = PropKit.get("upyunOperatorName");
        String OPERATOR_PWD = PropKit.get("upyunOperatorPwd");
        String imageUrl = PropKit.get("upyunImageUrl");
        String[] strNow = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString().split("-");
        int Y = Integer.parseInt(strNow[0]);
        int M = Integer.parseInt(strNow[1]);
        int D = Integer.parseInt(strNow[2]);
    	String savePath = "/"+ Y +"/"+ M+"-"+D +"/"+ UUID.randomUUID().toString().replace("-", "") +"."+imageType;
        File file = new File(fileAdress);
        UpYun upyun = new UpYun(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
        		
        Boolean result = upyun.writeFile(savePath, file);
        if(result){
        	return imageUrl + savePath;
        }else{
        	return "";
        }
    }


}
