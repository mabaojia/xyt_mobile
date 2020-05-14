package com.project.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class CodeUtil {

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static String getCode() throws Exception{
		
		String code=DateUtil.formatDate(new Date(), "yyMMddHHmmssSSS");
		code+=((int)(Math.random()*90) + 10);
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
	public static float getNumber(float number) throws Exception{
		
		BigDecimal bigdecimal=new BigDecimal(number);
		float results=bigdecimal.setScale(2,RoundingMode.HALF_EVEN).floatValue();
		System.out.println(results);
		return results;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static float getNumber(double number) throws Exception{
		
		BigDecimal bigdecimal=new BigDecimal(number);
		float results=bigdecimal.setScale(2,RoundingMode.HALF_EVEN).floatValue();
		System.out.println(results);
		return results;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static boolean isMobile(String mobile) {
			
		String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(mobile);
		return m.matches();
	}
}
