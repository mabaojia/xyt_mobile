package com.project.controller.wap;

import java.util.Map;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.OAuthApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;

/**
 * 
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/wap/diy", viewPath = "/wap/diy")
public class DiyController extends ApiController{

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	@Override
	public ApiConfig getApiConfig() {
		
		ApiConfig ac = new ApiConfig();
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appid"));
		ac.setAppSecret(PropKit.get("appsecret"));
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "xiaodaokeji"));
		return ac;
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
	public Map<String, String> getSignature(String url) {

		return OAuthApi.getSignature(url);
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public void index() throws Exception{
		
		keepPara();
		render("index.htm");
	}
}
