package com.fengdai.qa.meta;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMeta {
	
	String url;
	String method;
	HashMap<String, String> headers;
	HashMap<String, String> cookie;
	HashMap<String, Object> formdata;
	HashMap<String, Object> jsondata;
	
	public HashMap<String, Object> getFormdata() {
		return formdata;
	}
	public void setFormdata(HashMap<String, Object> formdata) {
		this.formdata = formdata;
	}
	public HashMap<String, Object> getJsondata() {
		return jsondata;
	}
	public void setJsondata(HashMap<String, Object> jsondata) {
		this.jsondata = jsondata;
	}

	public HashMap<String, String> getCookie() {
		return cookie;
	}
	public void setCookie(HashMap<String, String> cookie) {
		this.cookie = cookie;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public HashMap<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
	}
	
	

}
