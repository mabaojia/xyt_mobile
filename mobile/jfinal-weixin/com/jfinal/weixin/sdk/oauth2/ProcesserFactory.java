package com.jfinal.weixin.sdk.oauth2;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.weixin.sdk.oauth2.connector.WechatConnector;

public class ProcesserFactory {

	private static final Map<String, OauthConnector> oauths = new HashMap<String, OauthConnector>();

	static {
		oauths.put("wechat", new WechatConnector());
	}

	public static OauthConnector createProcesser(String key) {
		return oauths.get(key);
	}

}
