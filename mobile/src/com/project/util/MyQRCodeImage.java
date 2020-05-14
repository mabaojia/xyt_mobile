package com.project.util;

import java.awt.image.BufferedImage;
import jp.sourceforge.qrcode.data.QRCodeImage;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class MyQRCodeImage implements QRCodeImage {

	private BufferedImage bufferedImage;
    
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
    public MyQRCodeImage(BufferedImage bufferedImage){
        
    	this.bufferedImage = bufferedImage;
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    @Override
    public int getHeight() {
    	
        return bufferedImage.getHeight();
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    @Override
    public int getPixel(int arg0, int arg1) {

    	return bufferedImage.getRGB(arg0, arg1);
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    @Override
    public int getWidth() {

    	return bufferedImage.getWidth();
    }
}