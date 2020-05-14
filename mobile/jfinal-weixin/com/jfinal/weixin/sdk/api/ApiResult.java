/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.sdk.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiResult {
	
	private Map<String, Object> attrs;
	private String json;
	
	@SuppressWarnings("unchecked")
	public ApiResult(String jsonStr) {
		this.json = jsonStr;
		
		try {
			Map<String, Object> temp = new ObjectMapper().readValue(jsonStr, Map.class);
			this.attrs = temp;
			
			refreshAccessTokenIfInvalid();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void refreshAccessTokenIfInvalid() {
		if (isAccessTokenInvalid()){
			AccessTokenApi.removeAccessToken();
			AccessTokenApi.refreshAccessToken();
		}
	}
	
	public String getJson() {
		return json;
	}
	
	public String toString() {
		return getJson();
	}
	
	public boolean isSucceed() {
		Integer errorCode = getErrorCode();
		return (errorCode == null || errorCode == 0);
	}
	
	public Integer getErrorCode() {
		return (Integer)attrs.get("errcode");
	}
	
	public String getErrorMsg() {
		Integer errorCode = getErrorCode();
		if (errorCode != null) {
			String result = ReturnCode.get(errorCode);
			if (result != null)
				return result;
		}
		return (String)attrs.get("errmsg");
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		return (T)attrs.get(name);
	}
	
	public String getStr(String name) {
		return (String)attrs.get(name);
	}
	
	public Integer getInt(String name) {
		return (Integer)attrs.get(name);
	}
	
	public Long getLong(String name) {
		return (Long)attrs.get(name);
	}
	
	public BigInteger getBigInteger(String name) {
		return (BigInteger)attrs.get(name);
	}
	
	public Double getDouble(String name) {
		return (Double)attrs.get(name);
	}
	
	public BigDecimal getBigDecimal(String name) {
		return (BigDecimal)attrs.get(name);
	}
	
	public Boolean getBoolean(String name) {
		return (Boolean)attrs.get(name);
	}
	
	@SuppressWarnings("rawtypes")
	public List getList(String name) {
		return (List)attrs.get(name);
	}
	
	@SuppressWarnings("rawtypes")
	public Map getMap(String name) {
		return (Map)attrs.get(name);
	}

	public boolean isAccessTokenInvalid() {
		Integer ec = getErrorCode();
		return ec != null && (ec == 40001 || ec == 42001 || ec == 42002 || ec == 40014);
	}
}









