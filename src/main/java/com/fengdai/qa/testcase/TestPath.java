package com.fengdai.qa.testcase;
import static io.restassured.RestAssured.given;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;

import scala.Array;


public class TestPath {

	public static void main(String[] args) {
//		String var = given().param("code", "FDWMDSD001").when().get("http://www.360fengdai.com/loanDetail").asString();
//		System.out.println(var);
//		String var = given().param("phone", "18667906998").param("password", "wsc6821051").
//		post("http://www.360fengdai.com/account/user/login").getBody().asString();
//		String var ="{list:[{id:1,name:wsc}{id:2,name:wsk}]}";
		String var ="[{\"id\":1,\"name\":\"wsc\"},{\"id\":2,\"name\":\"wsk\"}]";
//		Object vString= JsonPath.read(var,"$[0]");
//		System.out.println(vString);
//		JSONObject.toJSONString(var);
//		String var ="{\"phone\":\"18667906998\",\"grantCode\":\"7a7872\"}";
		System.out.println(var);
		Object temp = io.restassured.path.json.JsonPath.from(var).get("[0]");
		System.out.println(temp);
		
		
	}
}
