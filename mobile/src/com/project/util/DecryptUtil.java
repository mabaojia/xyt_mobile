package com.project.util;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base64;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
public class DecryptUtil {
    
    private static final String AES ="AES";
    private static final String AES_CBC_PKCS7 ="AES/CBC/PKCS5Padding";
    
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public static byte[] decryptJsUserInfo(String encryptedData,String iv,String sessionKey) {
        
        try {
            byte[] data = Base64.decode(encryptedData);
            byte[] aseKey = Base64.decode(sessionKey);
            byte[] ivData = Base64.decode(iv);
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS7);
            Key sKeySpec = new SecretKeySpec(aseKey, AES);  
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIv(ivData));// 初始化 
            
            byte[] result = cipher.doFinal(data);
            return result;
        } catch (Exception e) {
            return null;
        }  
    }
    /**
     * 
     * 
     * 青岛小道福利信息技术服务有限公司
     * http://www.xiaodaofuli.com
     * 联系方式：137-9192-7167
     * 技术QQ：2511251392
     */
    public static AlgorithmParameters generateIv(byte[] iv) throws Exception{
    	
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");  
        params.init(new IvParameterSpec(iv));  
        return params;  
    }
}