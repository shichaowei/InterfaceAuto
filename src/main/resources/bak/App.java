package bak;

/**
 * Hello world!
 *
 */

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.fengdai.qa.util.AesUtil;
import com.fengdai.qa.util.EncrptUtil;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class App {
	static {
		RestAssured.baseURI = "http://opapi.dev.e-dewin.com";

	}

	public static String getrequestData(HashMap<String, String>testparam,JSONObject requestparm,String password) {
		//接口数据加入parameter
		JSONObject contentjson = new JSONObject();
		contentjson.put("parameter", testparam);
		requestparm.put("content", contentjson);
		String oldData = requestparm.toJSONString();
		System.out.println("oldData:"+oldData);

		//对测试数据进行加密和处理
//		String result = EncrptUtil.encrpt(oldData, password);
		String result = EncrptUtil.encrpt(oldData, password);
		System.out.println("result is : "+result);
		return result;

	}

	@Test
	public void testlogin() {
		String password="DWERP@#12$3458ta";
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
		//通用数据
		StringBuffer body = new StringBuffer();
		JSONObject requestparm= new JSONObject();
		requestparm.put("code", "10000");
		requestparm.put("type", "android");
		requestparm.put("device_id", "cb6685f3-4370-3664-8dbb-41655ff55644");
		requestparm.put("time", System.currentTimeMillis());
		requestparm.put("token", "android");
//		System.out.println(requestparm.toJSONString());

		//测试接口数据

		HashMap<String, String> loginparam= new HashMap<>();
		loginparam.put("loginMethod", "2");
		loginparam.put("packageName", "com.e_dewin.distribution");
		loginparam.put("appVersion", "8");
		loginparam.put("mobile", "18767191571");
		loginparam.put("password", "000000");
		loginparam.put("appType", "1");


		//发送数据拿到
		Response response = (Response) given().body(getrequestData(loginparam,requestparm,password)).when().post("apiservice").getBody();
		//校验数据
		HashMap sHashMap = JSONObject.parseObject(response.asString(), HashMap.class);
		System.out.println(sHashMap);
		String var =response.path("content");


		String token = response.path("token");
		System.out.println("揭秘后的数据："+AesUtil.aesDecrypt(var, password));
		HashMap contentmap = JSONObject.parseObject(AesUtil.aesDecrypt(var, password), HashMap.class);
		sHashMap.put("content", contentmap);
		System.out.println(sHashMap);
		System.out.println(JsonPath.from(JSONObject.toJSONString(sHashMap)).getString("content.data.userInfo.userRealName"));

		System.out.println("result is :"+response.asString());
		System.out.println("statuscode is:"+response.path("statusCode"));




		//把token值更新一下，已被后面使用 code值也更新为下一个接口的code
		requestparm.put("token", token);
		requestparm.put("code", "13007");
		//配送区域与门店信息()
		//测试接口数据
		HashMap<String, String> testdataparam= new HashMap<>();
		testdataparam.put("userId", "23ae51226d8f4c4392b2a06e36b257e5");
		testdataparam.put("busareaId", "f2025d7b165a4783b1287b2a97edd362");
		//发送数据拿到
		response = (Response) given().body(getrequestData(testdataparam,requestparm,password)).when().post("apiservice").getBody();
		System.out.println("解密之前的数据"+response.asString());
		var =response.path("content");
		System.out.println("揭秘后的数据："+AesUtil.aesDecrypt(var, password).toString());





	}



}
