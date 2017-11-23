package com.fengdai.qa.interfaceAuto;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.junit.Test;

import io.restassured.RestAssured;

public class App1 {
	static {
		RestAssured.baseURI = "http://10.200.141.34:81/";

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

		HashMap<String, String> body = new HashMap<>();
		body.put("fromName", "蜂贷机构账户");
		body.put("fromLoginName", "00000005");
		body.put("receive", "lianlian");
		body.put("toLoginName", "18667906998");
		body.put("toName", "魏士超");
		body.put("amount", "12");
		body.put("remarks", "魏士超");
		body.put("adjustmentType", "DIVIDE");
		String token = given().
			param("phone", "18611111111").
			param("password", "123456").
			param("verifyCode", "1234").
			param("templateCode", "fed_sms_dl_ht").
		when().
			post("account/user/passwd_smscode_login").getCookie("token");
		System.out.println(token);
		String var = given().contentType("application/json;charset=UTF-8").cookie("token", token).body(body).when().post("Finance/adjust_funds/post").getBody().asString();
		System.out.println(var);



	}



}
