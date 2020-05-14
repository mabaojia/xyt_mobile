package com.jfinal.weixin.sdk.api;

public class ApiConfigKit {
	
	private static final ThreadLocal<ApiConfig> tl = new ThreadLocal<ApiConfig>();
	
	private static boolean devMode = false;
	
	public static void setDevMode(boolean devMode) {
		ApiConfigKit.devMode = devMode;
	}
	
	public static boolean isDevMode() {
		return devMode;
	}
	
	public static void setThreadLocalApiConfig(ApiConfig apiConfig) {
		tl.set(apiConfig);
	}
	
	public static void removeThreadLocalApiConfig() {
		tl.remove();
	}
	
	public static ApiConfig getApiConfig() {
		ApiConfig result = tl.get();
		if (result == null)
			throw new IllegalStateException("需要事先使用 ApiConfigKit.setThreadLocalApiConfig(apiConfig) 将 ApiConfig对象存入，才可以调用 ApiConfigKit.getApiConfig() 方法");
		return result;
	}
}