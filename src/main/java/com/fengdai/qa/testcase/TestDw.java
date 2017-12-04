package com.fengdai.qa.testcase;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.ResourceUtils;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSONObject;
import com.fengdai.qa.meta.CaseMeta;
import com.fengdai.qa.meta.RequestMeta;
import com.fengdai.qa.meta.StepDetail;
import com.fengdai.qa.meta.ValidateMeta;
import com.fengdai.qa.util.Utils;
import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestDw {

	static HashMap<String, Object> bindmap= new HashMap<>();

	@BeforeTest
	public void befortest(){
//		RestAssured.baseURI = "http://opapi.dev.e-dewin.com";
	}

	@AfterTest
	public void aftertest(){


	}

	
	
	

	/**
	 * k值为要查找的value的路径
	 * @param stepDetail
	 * @param response
	 * @return
	 */
	public static HashMap<String, String> handleResponseBind (StepDetail stepDetail,Response response,HashMap<String, Object> bindmap) {
//		System.out.println("进入handleResponseBind"+bindmap);
//		System.out.println("response:"+response.asString());
		HashMap<String, String> bindresult = new HashMap<>();
		HashMap<String, String> var= stepDetail.getResponsebind();
//		System.out.println("要绑定的var is ----"+var);
		if(var !=null){
			var.forEach((k,v)->{
				if(v.matches("^cookie.*")){
					Pattern rp = Pattern.compile("^cookie.(.*)");
					Matcher matcher = rp.matcher(v);
					String path="";
					if(matcher.find()){
						path = matcher.group(1);
					}
					bindresult.put(k, (String) Utils.checkGetAll(response.getCookie(path),bindmap));
				}else if (v.matches("^content.*")) {

						Pattern rp = Pattern.compile("^content.(.*)");
						Matcher matcher = rp.matcher(v);
						String path="";
						if(matcher.find()){
							path = matcher.group(1);
						}
						boolean ifmiwenpath=false;

						try {
							response.getBody().path(path);
							ifmiwenpath = false;
						} catch (Exception e) {
							ifmiwenpath =true;
						}
						if(!ifmiwenpath) {
							if(String.class.isInstance(response.getBody().path(path)))
								bindresult.put(k, (String) Utils.checkGetAll(response.getBody().path(path), bindmap));
							else
								bindresult.put(k, response.getBody().path(path));
						}else {
							HashMap result = Utils.getmingwen(response.getBody().path("content"), "DWERP@#12$3458ta");
//							System.out.println(JSONObject.toJSONString(result));
							Matcher mat1= Pattern.compile("^content.(.*)").matcher(path);
							if(mat1.find()){
								path=mat1.group(1);
							}
//							System.out.println(path.replace(path, "content")+"------"+path);
//							System.out.println(JsonPath.from(JSONObject.toJSONString(result)).getString(path));
							bindresult.put(k, JsonPath.from(JSONObject.toJSONString(result)).getString(path));
							
						}

				}
			});
		}
		return bindresult;

	}


	/**
	 *
	 * 处理requestbind
	 * @param stepDetail
	 * @param bindmap
	 * @return
	 */
	public static HashMap<String, String> handleRequestBind (StepDetail stepDetail,HashMap<String, Object> bindmap) {
//		System.out.println("进入handleRequestBind"+bindmap);
		HashMap<String, String> bindresult = new HashMap<>();
		HashMap<String, String> var= stepDetail.getRequestbind();
		RequestMeta requestMeta = stepDetail.getRequest();
		if(var !=null){
			var.forEach((k,v)->{
				if(v.equals("request.jsondata")) {
						//处理jsondata 处理request中有$var ${sdfdfds()}情况
						HashMap<String, Object>  var1 = requestMeta.getJsondata();
						Utils.handleObject(var1, bindmap);
						bindresult.put(k,JSONObject.toJSONString(var1));
				}else 
				if(v.equals("request.formdata")) {
					//处理formdata 处理request中有$var ${sdfdfds()}情况
					HashMap<String, Object>  var1 = requestMeta.getParams();
					Utils.handleObject(var1, bindmap);
					bindresult.put(k,JSONObject.toJSONString(var1));
				}
			});
		}
		return bindresult;

	}


	@Test(dataProvider ="data")
	public void test(StepDetail step) throws InterruptedException{
		Response response = null;
    	StepDetail stepDetail =step;
    	RequestMeta request=stepDetail.getRequest();
    	RequestSpecification RS = given();
    	//处理requestbinds
    	HashMap<String, String> requestBinds = handleRequestBind(stepDetail,bindmap);
    	if(requestBinds != null)
    		bindmap.putAll(requestBinds);
    	switch (request.getMethod().toLowerCase()) {
			case "get":
				//getparams
		    	HashMap<String, Object> getparams = request.getParams();
		    	if(getparams!=null){
		    		getparams.forEach((k,v)->{
		    			v = Utils.handleObject(v, bindmap);
		    			RS.param(k, v);
		    		});
		    	}
				break;
			case "post":
				//form
		    	HashMap<String, Object> var = request.getParams();
		    	if(var!=null){
		        	var.forEach((k,v)->{
		        			v = Utils.handleObject(v, bindmap);
							RS.param(k, v);
		    		});
		    	}
		    	
		    	//json
		    	HashMap<String, Object> jsonvar = request.getJsondata();
		    	if(jsonvar!=null){
		    		jsonvar.forEach((k,v)->{
		    			v = Utils.handleObject(v, bindmap);
		    			jsonvar.put(k, v);
		    		});
		    		String jsonvarbody = JSONObject.toJSONString(jsonvar);
		    		//处理requesthandler
		        	HashMap<String,String> requesthandler = stepDetail.getRequesthandler();
		        	if(requesthandler!=null){
		        		requesthandler.forEach((k,v)->{
		            	    v = (String) Utils.handleObject(v, bindmap);
		            	    if(k.matches("^body$")) {
		            	    	RS.body(v);
		            	    }else if (k.matches("^cookie.*")) {
		            	    	Matcher matcher = Pattern.compile("^cookie.(.*)").matcher(v);
		            	    	if(matcher.find()){
		            	    		Object cookievalue=Utils.handleObject(v, bindmap);
		    						RS.cookie(matcher.group(1), cookievalue);
		            	    	}
							}
		        		});
		        	}
		    	}
				break;
	
			default:
				break;
		}
    	

    	//header
    	HashMap<String, String> var1= request.getHeaders();
    	if(var1!=null){
    	var1.forEach((k,v)->{
    			RS.header(k, Utils.checkGetAll(v, bindmap));
		});
    	}
    	//Cookie(最后是cookie 因为header有可能有cookie 以cookie的参数为最后的结果)
    	HashMap<String, String> var2= request.getCookie();
    	if(var2!=null){
        	var2.forEach((k,v)->{
        			RS.cookie(k,Utils.checkGetAll(v, bindmap));
    		});
    	}


    	//URL(增加URL参数化)
    	String[] testUrl = stepDetail.getRequest().getUrl().split("/");
    	StringBuffer urlHandled= new StringBuffer();
    	for (int i = 0; i < testUrl.length; i++) {
			urlHandled.append(Utils.checkGetAll(testUrl[i], bindmap));
			if(!(testUrl.length ==i+1))
				urlHandled.append("/");
		}
    	switch (stepDetail.getRequest().getMethod().toLowerCase()) {
			case "post":
				response = RS.post(urlHandled.toString());
				break;
			case "get":
				response = RS.get(urlHandled.toString());
				break;

			default:
				break;
		}
    	//绑定
    	HashMap<String, String> responseBinds = handleResponseBind(stepDetail, response,bindmap);
    	if(responseBinds != null)
    		bindmap.putAll(responseBinds);

    	//处理responsehandler
    	
    	HashMap sHashMap = JSONObject.parseObject(response.asString(), HashMap.class);
    	HashMap<String,String> responsehandler = stepDetail.getResponsehandler();
    	if(responsehandler!=null){
    		responsehandler.forEach((k,v)->{
        		sHashMap.put(k, Utils.checkGetAll(v, bindmap));
    		});
    	}






		System.out.println("请求的url:"+urlHandled);
		System.out.println("解密后的数据："+sHashMap);



    	//校验点
    	ArrayList<ValidateMeta> var3list = stepDetail.getValidate();
    	for(ValidateMeta var3:var3list){
    		if(var3.getCheck().matches("^body\\.(.*)")){
    			if(var3.getComparator().equals("eq")){
    				String actual = JsonPath.from(JSONObject.toJSONString(sHashMap)).getString(Utils.getJsonPath(var3.getCheck()));
    				Assert.assertEquals( actual,var3.getExpect());
//    				if(var3.getExpect().equals(expected)){
//    					System.out.println(var3.getExpect()+":check Ok");
//    				}
    			}
    		}else if (var3.getCheck().matches("^statusCode$")) {
    			if(var3.getComparator().equals("eq")){
    				Assert.assertEquals(Integer.valueOf(response.getStatusCode()), Integer.valueOf(var3.getExpect()));
    			}
			}
    	}

	}

	@DataProvider(name="data")
	public Iterator<StepDetail> dataprovider() throws FileNotFoundException{
		Yaml yaml = new Yaml();
//      URL url = Test.class.getClassLoader().getResource("test.yaml");

        //获取test.yaml文件中的配置数据，然后转换为obj，
        InputStream in = new FileInputStream(ResourceUtils.getFile("classpath:testYaml.yaml"));
//        System.out.println(((ArrayList<Object>)yaml.load(in)).get(0).toString());
        CaseMeta teStepMetas= yaml.loadAs(in, CaseMeta.class);
        return teStepMetas.getCasedata().iterator();
	}





}
