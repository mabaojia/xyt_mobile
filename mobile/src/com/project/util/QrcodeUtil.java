package com.project.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import jp.sourceforge.qrcode.QRCodeDecoder;

import com.swetake.util.Qrcode;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class QrcodeUtil {

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static void create(String url, File file) throws Exception{
		
		Qrcode x = new Qrcode();
        x.setQrcodeErrorCorrect('M');//纠错等级
        x.setQrcodeEncodeMode('B');//N代表数据;A代表a-A;B代表其他字符
        x.setQrcodeVersion(7);//版本
        String qrData = url;
        int width = 67 + 12 * (7-1);
        int height = 67 + 12 * (7-1);
        int pixoff = 2;//偏移量
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gs = bufferedImage.createGraphics();
        gs.setBackground(Color.WHITE);
        gs.setColor(Color.BLACK);
        gs.clearRect(0, 0, width, height);
        byte[] d =qrData.getBytes("utf-8");
        if (d.length>0 && d.length <120){
            boolean[][] s = x.calQrcode(d);
            for (int i=0;i<s.length;i++){
	            for (int j=0;j<s.length;j++){
	                if (s[j][i]) {
	                	gs.fillRect(j*3+pixoff,i*3+pixoff,3,3);
	                }
	            }
            }
        }
        gs.dispose();
        bufferedImage.flush();
        ImageIO.write(bufferedImage, "png", file);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static String content(File file) throws Exception{
		
		BufferedImage bufferedImage = ImageIO.read(file);
		QRCodeDecoder qrCodeDecoder = new QRCodeDecoder();
		String result = new String(qrCodeDecoder.decode(new MyQRCodeImage(bufferedImage)),"utf-8");
		System.out.println("二维码解析结果：" + result);
		return result;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static void main(String[] args) {

		try {
			create("https://zhuhai.xiaodaofuli.com/dcxt/wap/api/order/redirect/270", new File("d://qrcode.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
