package com.project.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import com.jfinal.kit.StrKit;
 
/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class ValidateCode {
	
    //图片的宽度
    private int width = 160;
    //图片的高度
    private int height = 40;
    //验证码字符个数
    private int codeCount = 6;
    //验证码干扰线数
    private int lineCount = 100;
    //验证码
    private String code = null;
    //验证码图片Buffer
    private BufferedImage buffImg = null;
    private char[] codeSequence = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    HttpSession session = null;
    
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public ValidateCode(HttpSession session) {
    	
    	this.session = session;
        this.createCode();
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public ValidateCode(int width, int height, int codeCount, int lineCount, HttpSession session) {
        
    	this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        this.session = session;
        this.createCode();
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public void createCode() {
        
    	int x = 0, fontHeight = 0, codeY = 0;
        int red = 0, green = 0, blue = 0;
        x = width / (codeCount + 2);
        fontHeight = height - 8;
        codeY = height - 8;
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        Random random = new Random();
        red = random.nextInt(255);
        while (red <= 225) {
        	red = random.nextInt(255);
		}
        green = random.nextInt(255);
        while (green <= 225) {
        	green = random.nextInt(255);
		}
        blue = random.nextInt(255);
        while (blue <= 225) {
        	blue = random.nextInt(255);
		}
        g.setColor(new Color(red, green, blue));
        g.fillRect(0, 0, width, height);
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        g.setFont(font);
        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width / 5);
            int ye = ys + random.nextInt(height / 5);
            red = random.nextInt(75);
            green = random.nextInt(75);
            blue = random.nextInt(75);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }
        StringBuffer randomCode = new StringBuffer();
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            red = random.nextInt(175);
            green = random.nextInt(175);
            blue = random.nextInt(175);
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x, codeY);
            randomCode.append(strRand);
        }
        code = randomCode.toString();
        session.setAttribute("validate_code", code);
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public void write(String path) throws IOException {
        
    	OutputStream sos = new FileOutputStream(path);
        this.write(sos);
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public void write(OutputStream sos) throws IOException {
        
    	ImageIO.write(buffImg, "png", sos);
        sos.close();
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public BufferedImage getBuffImg() {
        
    	return buffImg;
    }
 
    public String getCode() {
    	
        return code;
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public static boolean validate(HttpSession session, String code){
    	
    	Object session_code=session.getAttribute("validate_code");
    	if(session_code==null || StrKit.isBlank(session_code.toString())){
    		session.removeAttribute("validate_code");
    		return false;
    	}
    	if(StrKit.isBlank(code)){
    		session.removeAttribute("validate_code");
    		return false;
    	}
    	if(!code.equalsIgnoreCase(session_code.toString())){
    		session.removeAttribute("validate_code");
    		return false;
    	}
    	return true;
    }
}