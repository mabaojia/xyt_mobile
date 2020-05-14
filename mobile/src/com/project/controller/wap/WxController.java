package com.project.controller.wap;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.OAuthApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.jfinal.weixin.sdk.oauth2.OauthUser;
import com.jfinal.weixin.sdk.oauth2.connector.WechatConnector;
import com.project.aop.ExceptionInterceptor;
import com.project.model.User;
import com.project.util.MD5Util;

/**
 * 微信登录
 * 
 * 青岛小道福利信息技术服务有限公司
 * http://www.xiaodaofuli.com
 * 联系方式：137-9192-7167
 * 技术QQ：2511251392
 */
@ControllerBind(controllerKey = "/wap/wx")
public class WxController extends ApiController{

	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	public ApiConfig getApiConfig() {
		
		ApiConfig ac = new ApiConfig();
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("appSecret"));
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
	@ClearInterceptor(ClearLayer.ALL)
	public void index() throws Exception{
		
		PropKit.use("config.txt");
		String from=URLDecoder.decode(getPara("from"), "UTF-8");
		redirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + PropKit.get("appid").toString() + "&redirect_uri=" + URLEncoder.encode(PropKit.get("wxUrl").toString() + "/wap/wx/callback?from=" + URLEncoder.encode(from, "UTF-8"), "UTF-8") + "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");
		return;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	@ClearInterceptor(ClearLayer.ALL)
	@Before({ExceptionInterceptor.class, Tx.class})
	public void callback() throws Exception{
		
		PropKit.use("config.txt");
		WechatConnector wechat=new WechatConnector();
		OauthUser oauthUser=wechat.getOauthUser(getPara("code"), PropKit.get("appid"), PropKit.get("appsecret"));
		System.out.println("openid=" + oauthUser.getOpenId());
		if(StrKit.isBlank(oauthUser.getOpenId())){
			redirect("/wx?from=" + getPara("from"));
			return;
		}
		User user=User.getByOpenid(oauthUser.getOpenId());
		if(user==null){
			user=new User();
			user.set("name", oauthUser.getNickname().trim().replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*"))
				.set("img_url", oauthUser.getAvatar())
				.set("openid", oauthUser.getOpenId())
				.set("status", User.STATUS_ENABLE)
				.set("create_date", new Date())
				.save();
		}
		user.set("last_login_date", new Date())
			.update();
		setSessionAttr("user", user);
		setCookie("user_account", user.get("openid").toString(), 60 * 60 * 24 * 30);
		setCookie("user_password", MD5Util.getStringMD5("xiaodaokeji" + user.get("openid").toString() + "xiaodaokeji").toLowerCase(), 60 * 60 * 24 * 30);
		redirect(URLDecoder.decode(getPara("from"), "UTF-8"));
		return;
	}
	/**
	 * 
	 * 
	 * 青岛小道福利信息技术服务有限公司
	 * http://www.xiaodaofuli.com
	 * 联系方式：137-9192-7167
	 * 技术QQ：2511251392
	 */
	@ClearInterceptor(ClearLayer.ALL)
	public void layout() throws Exception{
		
		removeSessionAttr("user");
		removeCookie("user_account");
		removeCookie("user_password");
		redirect("/wap");
		return;
	}
}