package com.jfinal.weixin.sdk.api;

import com.jfinal.kit.HttpKit;

public class QrCodeApi {
	
	private static String ticketUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
	private static String qrCodeUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
	
	public static ApiResult getTicket(String parm){
		String json = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+parm+"}}}";
		String result = HttpKit.post(ticketUrl + AccessTokenApi.getAccessToken().getAccessToken(), json);
		return new ApiResult(result);
	}
	
	public static String getQrcodeurl(String ticket){
		return qrCodeUrl + ticket;
	}

	public static ApiResult getQrcode(String ticket){
		String result = HttpKit.post(qrCodeUrl + AccessTokenApi.getAccessToken().getAccessToken(), ticket);
		return new ApiResult(result);
	}
}
