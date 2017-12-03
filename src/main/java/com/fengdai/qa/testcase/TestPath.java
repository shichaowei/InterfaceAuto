package com.fengdai.qa.testcase;
import static io.restassured.RestAssured.given;

import java.util.ArrayList;

import com.jayway.jsonpath.JsonPath;

import scala.Array;


public class TestPath {

	public static void main(String[] args) {
//		String var = given().param("code", "FDWMDSD001").when().get("http://www.360fengdai.com/loanDetail").asString();
//		System.out.println(var);
//		System.out.println(given().param("phone", "18667906998").param("password", "wsc6821051").
//		post("http://www.360fengdai.com/account/user/login").getBody().asString());
//		String var ="[{id:1,name:wsc}{id:2,name:wsk}]";
//		Object vString= JsonPath.read(var,"$[0]");
//		String temp = io.restassured.path.json.JsonPath.from(var).prettyPrint();
//		System.out.println(temp);
		ArrayList<Integer> temp =new ArrayList();
		temp.add(1);
		temp.add(2);
		temp.add(3);
		for (int i : temp) {
			if(i==1){
				continue;
			}
			System.out.println(i);
		}
		
	}
}
