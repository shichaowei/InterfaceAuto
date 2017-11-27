package com.fengdai.qa.testcase;
import static io.restassured.RestAssured.given;
public class TestPath {

	public static void main(String[] args) {
		String var = given().param("code", "FDWMDSD001").when().get("http://www.360fengdai.com/loanDetail").asString();
		System.out.println(var);
	}
}
