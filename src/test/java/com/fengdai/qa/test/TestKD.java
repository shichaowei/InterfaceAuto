package com.fengdai.qa.test;

import static io.restassured.RestAssured.given;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class TestKD {
	public static JdbcTemplate jdbcTemplate;
	static {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://10.200.141.26:3306/fengdai_user?useUnicode=true&characterEncoding=utf-8");
		dataSource.setUsername("fdtest");
		dataSource.setPassword("Mysqltest@123098");

		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public static void processSql(String sql) {

		System.out.println(sql);
		jdbcTemplate.execute(sql);
	}

	public static boolean checkName(String cellphone) {
		System.out.println(jdbcTemplate
				.queryForMap("SELECT count(*) FROM fengdai_user.sys_user WHERE cellphone='" + cellphone + "'"));
		Long var = (Long) jdbcTemplate
				.queryForMap("SELECT count(*) FROM fengdai_user.sys_user WHERE cellphone='" + cellphone + "'")
				.get("count(1)");
		if (var != 0) {
			return true;
		}
		return false;
	}

	public static void addBMD(String cellphone) {
		String token = given().param("phone", "18611111111").param("password", "123456").param("verifyCode", "1234")
				.param("templateCode", "fed_sms_dl_ht").when()
				.post("http://10.200.141.10:81/account/user/passwd_smscode_login").getBody().path("token");

		String var = given().cookie("token", token).header("Content-Type", "application/json;charset=UTF-8")
				.body("{\"account\":\"" + cellphone
						+ "\",\"accountType\":\"WHITE\",\"username\":\"达飞\",\"validStart\":\"2018-02-28\",\"validEnd\":\"2019-12-30\",\"remarks\":null}")
				.put("http://10.200.141.10:81/RiskControl/sys_list").asString();
		System.out.println(var);
	}

	public static void deleteLoan(String cellphone) {

		given().cookie("SESSION", "133fb7c0-24e4-4041-9406-32c28ae6c7f1").param("deleteMode", "NEWONLINE")
				.param("deleteType", "deleteAllLoanByLoginname").param("param", cellphone)
				.post("http://10.200.141.37:8080/api/deleteUserInfo");
	}

	public static void main(String[] args) {
		RestAssured.baseURI = "http://10.200.141.10:85/";



		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

		for (int index = 2100; index < 6213; index++) {
			final int i = index;
			fixedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String cellphone = "1866791"+String.valueOf(i);
					if (true) {
						deleteLoan(cellphone);
						String sql = "UPDATE fengdai_user.`user_auth` SET `sesame_credit`='true', `operators`='true', `taobao`='true', `jingdong`='true', `accumulation_fund`='true', `face`='true', `contact`='true' WHERE user_id in (SELECT id FROM fengdai_user.sys_user WHERE cellphone like '"
								+ cellphone + "')";
						processSql(sql);
						addBMD(cellphone);
						// 登录
						Response var = given().param("phone", cellphone).param("smsCode", 1234).param("type", "fed_sms_dl_kd")
								.param("userSource", "QD000005").post("account/thirdUser/phoneLogin");
						System.out.println(var.getCookies());
						// 授信
						Response var1 = given().cookies(var.getCookies()).post("RiskControlWeb/loan_apply/submit_credit_kdqb");
						System.out.println(var1.asString());
						/*
						 * //借款 String body=
						 * "{\"certificateNumber\":\"410581199007129054\",\"fullname\":\"魏士超\",\"loginName\":\"18667906998\","
						 * + "\"mobilePhone\":\"18667906998\",\"loanType\":\"withdrawCashInstallment\","
						 * +
						 * "\"subProductId\":\"94d8ea1ea5604c61ba22830372ae1ff6\",\"loanAmount\":1000,"
						 * + "\"applyNumber\":3,\"creditFlag\":false,\"creditCardFlag\":true," +
						 * "\"applyChannel\":\"QD000005\",\"rechargeNo\":null,\"rechargeName\":null,\"loanUsage\":\"1\"}";
						 * given().cookies(var.getCookies()).post(
						 * "RiskControlWeb/loan_apply/submit_loan");
						 */
					}
				}

			});

		}
		fixedThreadPool.shutdown();
		while (true) {
			if (fixedThreadPool.isTerminated()) {
				break;
			}
		}
	}

}
