package com.jfinal.weixin.sdk.jfinal;

import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.project.common.BaseController;

/**
 * 所有使用 Api 的 controller 需要继承此类
 */
@Before(ApiInterceptor.class)
public abstract class ApiController extends BaseController {
	public abstract ApiConfig getApiConfig();
	
	public abstract Map<String,String> getSignature(String url);
}
