package com.project.weixin.pay;

import java.io.InputStream;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public final class Tools {  
	
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
    public static final String inputStream2String(InputStream in) throws Exception{  
       
    	if(in == null){
    		return "";  
    	}
        StringBuffer out = new StringBuffer();  
        byte[] b = new byte[4096];  
        for (int n; (n = in.read(b)) != -1;) {  
            out.append(new String(b, 0, n, "UTF-8"));  
        }  
        return out.toString();  
    }  
} 
