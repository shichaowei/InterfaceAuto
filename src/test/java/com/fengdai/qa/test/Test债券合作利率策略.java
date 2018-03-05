package com.fengdai.qa.test;

import static io.restassured.RestAssured.given;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
public class Test债券合作利率策略 {

	public static void main(String[] args) {
		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.addHeader("Content-Type", "application/json;charset=UTF-8");
		RequestSpecification requestSpec = builder.build();

		HashMap<String, Object> postbody = new HashMap<>();
		ArrayList<Object> interestlist = new ArrayList<>();
		for(int i=1;i<25;i++) {
			BigDecimal var1= new BigDecimal(Math.random());
			HashMap<String, Object> interestvar= new HashMap<>();
			interestvar.put("term", i);
			interestvar.put("monthRate",var1.setScale(3,RoundingMode.HALF_UP));
			interestlist.add(interestvar);
		}
		postbody.put("name", "烟草通1");
		postbody.put("detail",interestlist);

		String var= given().cookie("token","83C2F22D4B674B1F984CF2A858D5CBBB.87A0943C57845CAF64F7FCF70621F641")
				.spec(requestSpec)
				.body(postbody)
				.post("http://10.200.141.10:81/RiskControl/dts/platform_interest").asString();
			System.out.println(var);

		/**
		 * 多次新增测试翻页器
		 */
//		for(int i=0;i<100;i++) {
//		String var= given().cookie("token","0288B1658E0147F09728D3A069FC2DFC.22ED6639E550E1C1FABF477E99A156B9")
//			.spec(requestSpec)
//			.body("{\"name\":\"</script>\",\"detail\":[{\"term\":10,\"monthRate\":0.1}]}")
//			.post("http://10.200.141.56:81/RiskControl/dts/platform_interest").asString();
//		System.out.println(var);
//		}
	}

}
