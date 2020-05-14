/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.sdk.api;

public class ApiConfig {
	
	private String token = null;
	private String appId = null;
	private String appSecret = null;
	private String encodingAesKey = null;
	private boolean messageEncrypt = false;
	
	public ApiConfig() {
		
	}
	
	public ApiConfig(String token) {
		setToken(token);
	}
	
	public ApiConfig(String token, String appId, String appSecret) {
		setToken(token);
		setAppId(appId);
		setAppSecret(appSecret);
	}
	
	public ApiConfig(String token, String appId, String appSecret, boolean messageEncrypt, String encodingAesKey) {
		setToken(token);
		setAppId(appId);
		setAppSecret(appSecret);
		setEncryptMessage(messageEncrypt);
		setEncodingAesKey(encodingAesKey);
	}
	
	public String getToken() {
		if (token == null)
			throw new IllegalStateException("token 未被赋值");
		return token;
	}
	
	public void setToken(String token) {
		if (token == null)
			throw new IllegalArgumentException("token 值不能为 null");
		this.token = token;
	}
	
	public String getAppId() {
		if (appId == null)
			throw new IllegalStateException("appId 未被赋值");
		return appId;
	}
	
	public void setAppId(String appId) {
		if (appId == null)
			throw new IllegalArgumentException("appId 值不能为 null");
		this.appId = appId;
	}
	
	public String getAppSecret() {
		if (appSecret == null)
			throw new IllegalStateException("appSecret 未被赋值");
		return appSecret;
	}
	
	public void setAppSecret(String appSecret) {
		if (appSecret == null)
			throw new IllegalArgumentException("appSecret 值不能为 null");
		this.appSecret = appSecret;
	}
	
	public String getEncodingAesKey() {
		if (encodingAesKey == null)
			throw new IllegalStateException("encodingAesKey 未被赋值");
		return encodingAesKey;
	}
	
	public void setEncodingAesKey(String encodingAesKey) {
		if (encodingAesKey == null)
			throw new IllegalArgumentException("encodingAesKey 值不能为 null");
		this.encodingAesKey = encodingAesKey;
	}
	
	public boolean isEncryptMessage() {
		return messageEncrypt;
	}

	public void setEncryptMessage(boolean messageEncrypt) {
		this.messageEncrypt = messageEncrypt;
	}
}


