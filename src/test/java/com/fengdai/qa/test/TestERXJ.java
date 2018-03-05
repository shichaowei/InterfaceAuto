package com.fengdai.qa.test;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
public class TestERXJ {

	public static void main(String[] args) {
		//众安风险名单采集
//		 Response response = given().param("code", "zhongan_query_rank").param("userId", "A815180818754084AD5C37B62B92170C").param("acquisitionFashion", "polling_gradient_acquisition").
//			post("http://10.200.141.17:9050/inner/thirdparty_thirdInterface/collectByCode");
//		Response response =given().post("http://10.200.141.17:9050/inner/thirdparty_thirdInterface/collectByCode?code=zhongan_query_rank&userId=A815180818754084AD5C37B62B92170C&acquisitionFashion=polling_gradient_acquisition");
		Response response =given().
				post("http://10.200.141.17:9050/inner/thirdparty_thirdInterface/collectByCode?code=nfcs&userId=A815180818754084AD5C37B62B92170C&acquisitionFashion=polling_gradient_acquisition");
		System.out.println(response.asString());

	}

}
