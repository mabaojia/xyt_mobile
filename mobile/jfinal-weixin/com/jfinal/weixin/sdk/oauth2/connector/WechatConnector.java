package com.jfinal.weixin.sdk.oauth2.connector;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.weixin.sdk.oauth2.OauthConnector;
import com.jfinal.weixin.sdk.oauth2.OauthUser;

public class WechatConnector extends OauthConnector {

	public String createAuthorizeUrl(String state) {

		StringBuilder urlBuilder = new StringBuilder("https://open.weixin.qq.com/connect/qrconnect?");
		urlBuilder.append("response_type=code");
		urlBuilder.append("&scope=snsapi_login");
		urlBuilder.append("&appid=" + getClientId());
		urlBuilder.append("&redirect_uri=" + getRedirectUri());
		urlBuilder.append("&state=" + state);
		urlBuilder.append("#wechat_redirect");
		return urlBuilder.toString();
	}

	public JSONObject getAccessToken(String code, String app_id, String app_secret) {

		StringBuilder urlBuilder = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token?");
		urlBuilder.append("grant_type=authorization_code");
		urlBuilder.append("&appid="+app_id);
		urlBuilder.append("&secret="+app_secret);
		urlBuilder.append("&code=" + code);
		String url = urlBuilder.toString();
		String httpString = httpGet(url);
		return JSONObject.parseObject(httpString);
	}

	@Override
	public OauthUser getOauthUser(String code, String app_id, String app_secret) {

		JSONObject tokenJson = getAccessToken(code,app_id,app_secret);
		String accessToken = tokenJson.getString("access_token");
		String openId = tokenJson.getString("openid");
		String url = "https://api.weixin.qq.com/sns/userinfo?" + "access_token=" + accessToken + "&openid=" + openId;
		String httpString = httpGet(url);
		OauthUser user = new OauthUser();
		user.setUnionid(tokenJson.getString("unionid"));
		JSONObject json = JSONObject.parseObject(httpString);
		user.setAccessToken(accessToken);
		user.setAvatar(json.getString("headimgurl"));
		user.setNickname(json.getString("nickname"));
		user.setOpenId(openId);
		int sex = json.getIntValue("sex");
		user.setGender(sex == 1 ? "male" : "female");
		user.setSource(getName());
		return user;
	}
}
