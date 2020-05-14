package com.jfinal.weixin.sdk.api;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.kit.ParaMap;

public class OAuthApi {

	private static String getToken = "https://api.weixin.qq.com/sns/oauth2/access_token?";
	private static String getJsApiTicket = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?";
	private static String ticketTableName = "db_weixin_jsapi_ticket";
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static ApiResult getWebOAuthToken(String code){
		
		ApiConfig ac = ApiConfigKit.getApiConfig();
		ParaMap pm = ParaMap.create();
		pm.put("appid", ac.getAppId());
		pm.put("secret", ac.getAppSecret());
		pm.put("code", code);
		pm.put("grant_type", "authorization_code");
		return new ApiResult(HttpKit.get(getToken, pm.getData()));
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static String getJsApiTicket(){
		
		ApiConfig ac = ApiConfigKit.getApiConfig();
		String access_token = AccessTokenApi.getAccessToken().getAccessToken();
		List<Record> list = Db.find("select * from "+ticketTableName+" where app_id=?", ac.getAppId());
		Record record = null;
		if(list != null && list.size()==1){
			record = list.get(0);
		}else if(list != null && list.size() > 1){
			Db.update("delete from "+ticketTableName+" where app_id=?", ac.getAppId());
		}
		Date now = new Date();
		if(record != null){
			if(StrKit.notBlank(record.get("js_api_ticket").toString()) && access_token.equals(record.get("account_token").toString())){
				Date end = record.getDate("create_date");
				long endTime = end.getTime();
				if (now.getTime() < (endTime + (7200 + 5) * 1000)) {
					return record.get("js_api_ticket").toString();
				}
			}
		}else{
			record = new Record();
			record.set("app_id", ac.getAppId());
		}
		record.set("account_token", access_token);
		record.set("create_date", now);
		ParaMap pm = ParaMap.create();
		pm.put("access_token", access_token);
		pm.put("type", "jsapi");
		String json = HttpKit.get(getJsApiTicket, pm.getData());
		Ticket ticket = new Ticket(json);
		record.set("js_api_ticket", ticket.getTicket());
		if(record.get("id") != null){
			Db.update(ticketTableName, record);
		}else{
			Db.save(ticketTableName, record);
		}
		return record.get("js_api_ticket").toString();
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static Map<String,String> getSignature(String url){
		
		url = urlFilter(url);
		ApiConfig ac = ApiConfigKit.getApiConfig();
		Map<String, String> ret = new HashMap<String, String>();
		String nonce_str = UUID.randomUUID().toString().replace("-", "");
		String timestamp = String.valueOf(System.currentTimeMillis()/1000);
		String jsapi_ticket = getJsApiTicket();
		String signature = "";
		String string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", ac.getAppId());
        System.out.println("\n\n"
        		+"appId="+ac.getAppId()+"\n"
        		+"account_token="+ AccessTokenApi.getAccessToken().getAccessToken()+"\n"
        		+"jsapi_ticket="+jsapi_ticket+"\n"
        		+"nonceStr="+nonce_str+"\n"
        		+"timestamp="+timestamp+"\n"
        		+"url="+ url+"\n"
        		+"signature="+signature+"\n"
        		+"\n\n"
        		);
		return ret;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	private static String byteToHex(final byte[] hash) {
		
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
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
	private static String urlFilter(String url){
		if(StrKit.isBlank(url)){
			return url;
		}
		if(url.indexOf("#") != -1){
			url = url.substring(0,url.indexOf("#"));
		}
		return url;
	}
}
