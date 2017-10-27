package com.fengdai.qa.interfaceAuto;

/**
 * Hello world!
 *
 */

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.junit.Test;

import io.restassured.RestAssured;

public class App {
	static {
		RestAssured.baseURI = "http://10.200.141.34:81";
		RestAssured.port = 81;
	}

	@Test
	public void testlogin() {
		given().
			param("phone", "18611111111").
			param("password", "123456").
			param("verifyCode", "1234").
			param("templateCode", "fed_sms_dl_ht").
		when().
			post("/account/user/passwd_smscode_login").
		then().
			body(containsString("18611111111"));
	}
}
