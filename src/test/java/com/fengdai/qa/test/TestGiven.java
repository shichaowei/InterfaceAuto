package com.fengdai.qa.test;

import static io.restassured.RestAssured.given;

import java.io.UnsupportedEncodingException;
public class TestGiven {



	public static void main(String[] args) throws UnsupportedEncodingException {

//		Response response = given().multiPart("fileName",new File("F:\\FDAutoTest\\autotest\\InterfaceAuto\\src\\main\\resources\\koudai.har")).
//				post("http://127.0.0.1:8090/api/caseupload").andReturn();
//		System.out.println(response.asString());

		String var ="{\"certificateNumber\":\"410581199007129054\",\"fullname\":\"魏士超\",\"loginName\":\"18667906998\",\"mobilePhone\":\"18667906998\",\"loanType\":\"withdrawCash\",\"subProductId\":\"94d8ea1ea5604c61ba22830372ae1ff6\",\"loanAmount\":600,\"creditFlag\":false,\"creditCardFlag\":true,\"applyNumber\":1,\"applyChannel\":\"QD000005\",\"rechargeNo\":null,\"rechargeName\":null,\"loanUsage\":\"1\"}";
		System.out.println(var);
		String var1 = given().cookie("token","4BCED2EC73B745E79D2A2D6FE9FFDFDD.4FA444CF09CD9188E511BD5C7AFF4940").body(var).post("http://10.200.141.10:85/RiskControlWeb/loan_apply/submit_loan").asString();
		System.out.println(var1);
//		Response temp = given().param("userName", "fengdai").param("password", "fengdai2017")
//				.param("ifRemember", "on").post("http://10.200.141.52:8081/schedule-admin/login");
//		Response temp1 = given().param("id", Integer.valueOf("1022")).cookies(temp.getCookies()).post("http://10.200.141.52:8081/schedule-admin/jobinfo/trigger");
//		System.out.println(temp1.asString());

//		new LogPrepareUtil() {
//
//			@Override
//			public void process() {
//				// TODO Auto-generated method stub
//
//				Response response = given().cookie("token","D6960D67C79943F4B9879DCB1B3CAEDE.A9195A65CE84861E3168B0ED9470FA74").
//						get("http://10.200.141.72/ReportWeb/riskcontrol_urge/entrust_order_list?pageNum=1&pageSize=10");
//				System.out.println(response.asString());
//			}
//		};
//		String dingdingbody="{\"msgtype\":\"text\",\"text\":{\"content\":\"修改时间:%s\"}}";
////		String body = new String(.getBytes(),"utf-8");
//		Response response = given().cookie("token","D6960D67C79943F4B9879DCB1B3CAEDE.A9195A65CE84861E3168B0ED9470FA74").
//				get("http://10.200.141.65:92/ReportWeb/riskcontrol_urge/entrust_order_list?pageNum=1&pageSize=10");
//		System.out.println(response.asString());
//		long endTime=System.currentTimeMillis();

	}

}
