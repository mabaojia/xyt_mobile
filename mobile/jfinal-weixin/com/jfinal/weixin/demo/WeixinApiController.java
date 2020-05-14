package com.jfinal.weixin.demo;

import java.util.Map;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.MenuApi;
import com.jfinal.weixin.sdk.api.OAuthApi;
import com.jfinal.weixin.sdk.api.UserApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;

@ControllerBind(controllerKey = "/wechat/api", viewPath = "/wechat/api")
public class WeixinApiController extends ApiController {
	
	public ApiConfig getApiConfig() {
		
		ApiConfig ac = new ApiConfig();
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("appSecret"));
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		return ac;
	}
	
	public void getMenu() {
		
		ApiResult apiResult = MenuApi.getMenu();
		if (apiResult.isSucceed())
			renderText(apiResult.getJson());
		else
			renderText(apiResult.getErrorMsg());
	}
	
	public void getFollowers() {
		
		ApiResult apiResult = UserApi.getFollows();
		renderText(apiResult.getJson());
	}
	
	public Map<String,String> getSignature(String url){
		
		return OAuthApi.getSignature(url);
	}
}

