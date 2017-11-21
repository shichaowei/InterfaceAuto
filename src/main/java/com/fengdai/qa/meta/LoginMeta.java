package com.fengdai.qa.meta;

import com.google.gson.JsonObject;
import com.alibaba.fastjson.JSONObject;

public class LoginMeta {
	String loginMethod;
	String mobile;
	String verifyCode;
	String password;
	String appType;
	String packageName;
	String appVersion;
	public String getLoginMethod() {
		return loginMethod;
	}
	public void setLoginMethod(String loginMethod) {
		this.loginMethod = loginMethod;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	public static void main(String[] args) {
		LoginMeta var = new LoginMeta();
		var.setLoginMethod("2");
		System.out.println(JSONObject.toJSON(var).toString());
	}
	
}
