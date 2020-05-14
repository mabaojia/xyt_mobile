package com.project.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class HandleDpi {
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static void main(String[] args) throws Exception{
		
	    String path = "d://test.jpg";
	    String path1 = "d://test1.jpg";
	    File file = new File(path);
	    File file1 = new File(path1);
	    handleDpi(file, file1, 300, 300);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static void handleDpi(File file, File file1, int xDensity, int yDensity) throws Exception{
		
        BufferedImage image = ImageIO.read(file);
        JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(new FileOutputStream(file1));
        JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(image);
        jpegEncodeParam.setDensityUnit(JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
        jpegEncoder.setJPEGEncodeParam(jpegEncodeParam);
        jpegEncodeParam.setQuality(0.75f, false);
        jpegEncodeParam.setXDensity(xDensity);
        jpegEncodeParam.setYDensity(yDensity);
        jpegEncoder.encode(image, jpegEncodeParam);
        image.flush();
	}
}
