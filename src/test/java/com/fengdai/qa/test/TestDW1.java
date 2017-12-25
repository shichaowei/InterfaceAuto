package com.fengdai.qa.test;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONObject;
import com.fengdai.qa.util.Utils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
public class TestDW1 {
	
	public static String getmiwen(String var) {
		String var1=JSONObject.toJSONString(Utils.getmingwen(var, "DWERP@#12$3458ta"));
		System.out.println(var1);
		return var1;
	}
	
	public static void main(String[] args) {
//		String var ="/rpF2brX65nfprNaT1n97Tosr6xVPPG/v81ymXnOa7W+PfuBsgHqmzfAMREqtOkXriPid2VoUCg4839Oq2V6JiQq9bhOPgIEBZndEBGiKOZYlne3kNs3wgFXPYMBPzckr4hEnsZYObcbdHUq+axN/HhRDHRyBodBguP2SlMzoojcyZlDUIaZpfpsB1w71LAEo8i2QY+IeD11Zad0S21WqQ__";
//		getmiwen(var);
		RestAssured.baseURI = "http://opapi.dev.e-dewin.com";
		HashMap<String, Object> contentMap = new HashMap<>();
		HashMap<String, String> parametermap = new HashMap<>();
		parametermap.put("loginMethod", "2");
		parametermap.put("packageName", "com.e_dewin.distribution");
		parametermap.put("appVersion", "9");
		parametermap.put("mobile", "18767191571");
		parametermap.put("password", "000000");
		parametermap.put("appType", "1");
		contentMap.put("parameter", parametermap);
		HashMap<String, Object> requestbody = new HashMap<>();
		requestbody.put("code", "10000");
		requestbody.put("type", "android");
		requestbody.put("device_id", "cb6685f3-4370-3664-8dbb-41655ff55644");
		requestbody.put("time", Utils.getcurrenttime());
		requestbody.put("token", "");
		requestbody.put("content", contentMap);
		System.out.println(Utils.handlerdewein(JSONObject.toJSONString(requestbody),"DWERP@#12$3458ta"));
		ResponseBody responsebody = given().body(Utils.handlerdewein(JSONObject.toJSONString(requestbody),"DWERP@#12$3458ta")).post("apiservice").getBody();
		String token = responsebody.path("token");
		HashMap contentresponsejiemi = Utils.getmingwen(responsebody.path("content"), "DWERP@#12$3458ta");
		System.out.println(JSONObject.toJSONString(contentresponsejiemi));
		//((HashMap)(((HashMap)contentresponsejiemi.get("data")).get("userInfo"))).get("userId");
		String userId = JsonPath.from(JSONObject.toJSONString(contentresponsejiemi)).getString("data.userInfo.userId");
		System.out.println(userId);
		//qinngli data
		parametermap.clear();
		contentMap.clear();
		
		
		
		
		
	}

}
