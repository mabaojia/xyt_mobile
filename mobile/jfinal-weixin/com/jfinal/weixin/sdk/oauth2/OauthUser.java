package com.jfinal.weixin.sdk.oauth2;

import java.io.Serializable;

public class OauthUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String openId;
	private String nickname;
	private String avatar;
	private String gender;
	private String source;
	private String unionid;
	private String accessToken;
	private int subscribe;
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getUnionid() {
		return unionid;
	}

	public int getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
