package com.fengdai.qa.interfaceAuto;

/**
 * Hello world!
 *
 */

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.fengdai.qa.util.AesUtil;
import com.fengdai.qa.util.EncrptUtil;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class App2 {
	static {
		RestAssured.baseURI = "http://opapib.dev.e-dewin.com";
	}

	public static String getrequestData(HashMap<String, String>testparam,JSONObject requestparm,String password) {
		//接口数据加入parameter
		JSONObject contentjson = new JSONObject();
		contentjson.put("parameter", testparam);
		requestparm.put("content", contentjson);
		String oldData = requestparm.toJSONString();
		System.out.println("oldData:"+oldData);
		System.out.println("password:"+password);

		//对测试数据进行加密和处理
		String result = EncrptUtil.encrpt(oldData, password);
		System.out.println("result is : "+result);
		return result;

	}

	@Test
	public void testlogin() {
		//通用数据
//		StringBuffer body = new StringBuffer();
		JSONObject requestparm= new JSONObject();
		requestparm.put("code", "10000");
		requestparm.put("type", "android");
		requestparm.put("device_id", "cb6685f3-4370-3664-8dbb-41655ff55644");
		requestparm.put("time", System.currentTimeMillis());
		requestparm.put("token", "android");
//		System.out.println(requestparm.toJSONString());

		// 测试接口数据
		String password="DWERP@#12$3458ta";
		HashMap<String, String> loginparam= new HashMap<>();
		loginparam.put("loginMethod", "2");
		loginparam.put("packageName", "com.e_dewin.distribution");
		loginparam.put("appVersion", "9");
		loginparam.put("mobile", "18767191571");
		loginparam.put("password", "000000");
		loginparam.put("appType", "1");


		// 发送数据拿到
		Response response = (Response) given().body(getrequestData(loginparam,requestparm,password)).when().post("apiservice").getBody();
		String var =response.path("content");
		String token = response.path("token");
		System.out.println("揭秘后的数据："+AesUtil.aesDecrypt(var, password));
		System.out.println("token is :"+token);


		// 把token值更新一下，已被后面使用 code值也更新为下一个接口的code
		requestparm.put("token", token);
		requestparm.put("code", "13007");
		requestparm.put("time", System.currentTimeMillis());
		//配送区域与门店信息()
		//测试接口数据
		HashMap<String, String> testdataparam= new HashMap<>();
		testdataparam.put("userId", "75627cbe29e348fe875b7984ff324303");
		testdataparam.put("busareaId", "f2025d7b165a4783b1287b2a97edd362");
		//发送数据拿到
		response = (Response) given().body(getrequestData(testdataparam,requestparm,password)).when().post("apiservice").getBody();
		System.out.println("response"+response.asString());
		String statuscode = response.path("statusCode");
		if(statuscode.equals("600")) {
			System.out.println("获取下单页面成功");
		}
		var =response.path("content");
//		System.out.println("揭秘后的数据："+AesUtil.aesDecrypt(var, password));





	}




}
