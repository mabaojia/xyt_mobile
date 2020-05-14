/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.sdk.api;

import java.util.Date;
import java.util.Map;

import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.kit.ParaMap;

public class AccessTokenApi {
	
	private static String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
	
	private static AccessToken accessToken;
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static AccessToken getAccessToken() {
		
		accessToken = getTokenWX(ApiConfigKit.getApiConfig());
		if(accessToken != null && accessToken.isAvailable()){
			return accessToken;
		}
		refreshAccessToken();
		return accessToken;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public static void refreshAccessToken() {
		
		accessToken = requestAccessToken();
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	private static synchronized AccessToken requestAccessToken() {
		
		AccessToken result = null;
		ApiConfig ac = ApiConfigKit.getApiConfig();
		for (int i=0; i<3; i++) {
			String appId = ac.getAppId();
			String appSecret = ac.getAppSecret();
			Map<String, String> queryParas = ParaMap.create("appid", appId).put("secret", appSecret).getData();
			String json = HttpKit.get(url, queryParas);
			result = new AccessToken(json);
			if (result.isAvailable()){
				Date time = new Date();
				String update_sql = "update db_weixin_account set access_token=?,create_date=? where app_id=?";
				Db.update(update_sql, result.getAccessToken(), time, ac.getAppId());
				break;
			}
		}
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
	public static void removeAccessToken(){
		
		accessToken = null;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	private static AccessToken getTokenWX(ApiConfig ac){
		
		AccessToken result = null;
		Record rs = Db.findFirst("select * from db_weixin_account where app_id=?", ac.getAppId());
		if(rs == null || StrKit.isBlank(rs.get("access_token").toString())){
			return result;
		}
		Date start = rs.getDate("create_date");
		Date end = new Date();
		if ((end.getTime() - start.getTime()) / 1000 > 3600) {
			return result;
		}
		long expires_in = (end.getTime() - start.getTime());
		String json = "{\"access_token\":\"" + rs.get("access_token").toString() + "\",\"expires_in\":" + expires_in + "}";
		result = new AccessToken(json);
		if (result.isAvailable()){
			return result;
		}else{
			return null;
		}
	}
}
