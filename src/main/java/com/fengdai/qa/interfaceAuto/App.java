package com.fengdai.qa.interfaceAuto;

/**
 * Hello world!
 *
 */

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.fengdai.qa.util.AesUtil;
import com.fengdai.qa.util.EncrptUtil;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class App {
	static {
		RestAssured.baseURI = "http://test.dev.e-dewin.com";
	}

	@Test
	public void testlogin() {
		
//		given().
//			param("phone", "18667906998").
//			param("password", "wsc6821051").
//		when().
//			post("account/user/login").
//		then().
//			body("phone",equalTo("18667906998")).
//			statusCode(200).
//			header("Content-Type", "application/json").
//			time(lessThan(4000L));
		
//		RequestSpecification var = given().param("phone", "18667906998");
//		String token = var.param("password", "wsc6821051").when().
//		post("account/user/login").getCookies().get("token");
//		System.out.println(token);
		StringBuffer body = new StringBuffer();
		JSONObject requestparm= new JSONObject();
		requestparm.put("code", "10000");
		requestparm.put("type", "android");
		requestparm.put("device_id", "cb6685f3-4370-3664-8dbb-41655ff55644");
		requestparm.put("time", System.currentTimeMillis());
		requestparm.put("token", "android");
//		System.out.println(requestparm.toJSONString());
		
		String password="DWERP@#12$3458ta";
		HashMap<String, String> loginparam= new HashMap<>();
		loginparam.put("loginMethod", "2");
		loginparam.put("packageName", "com.e_dewin.distribution");
		loginparam.put("appVersion", "8");
		loginparam.put("mobile", "18767191571");
		loginparam.put("password", "000000");
		loginparam.put("appType", "1");
		
		JSONObject contentjson = new JSONObject();
		contentjson.put("parameter", loginparam);
		requestparm.put("content", contentjson);
		String oldData = requestparm.toJSONString();
		System.out.println(oldData);
		
		String result = EncrptUtil.encrpt(oldData, password);
		System.out.println(result);
		
		
		String var = given().body(result).when().post("apiservice").getBody().path("content");
		String token = given().body(result).when().post("apiservice").getBody().path("token");
		System.out.println(AesUtil.aesDecrypt(var, password));
		System.out.println(token);
		
		requestparm.put("token", token);
		
		
		
		
		
		
		
	}



}
