package com.fengdai.qa.testcase;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
import com.fengdai.qa.meta.StepMeta;
import com.fengdai.qa.meta.ValidateMeta;
import com.fengdai.qa.util.Utils;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestDw {

	static HashMap<String, Object> bindmap= new HashMap<>();

	@BeforeTest
	public void befortest(){
		RestAssured.baseURI = "http://opapi.dev.e-dewin.com";
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
		HashMap<String, String> bindresult = new HashMap<>();
		HashMap<String, String> var= stepDetail.getResponsebind();
		if(var !=null){
			var.forEach((k,v)->{
				if(v.matches("^cookie.*")){
					bindresult.put(k, (String) Utils.checkGetAll(response.getCookie(k),bindmap));
				}else if (v.matches("^content.*")) {
					try {
						if(String.class.isInstance(response.getBody().path(k)))
							bindresult.put(k, (String) Utils.checkGetAll(response.getBody().path(k), bindmap));
						else
							bindresult.put(k, response.getBody().path(k));
					} catch (JsonPathException e) {
//						System.out.println("response is :"+response.getBody().asString());
						e.printStackTrace();
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
		HashMap<String, String> bindresult = new HashMap<>();
		HashMap<String, String> var= stepDetail.getRequestbind();
		RequestMeta requestMeta = stepDetail.getRequest();
		if(var !=null){
			var.forEach((k,v)->{
				if(v.equals("request.jsondata")) {
						//处理jsondata 处理request中有$var ${sdfdfds()}情况
						HashMap<String, Object>  var1 = requestMeta.getJsondata();
						HashMap<String, Object> jsondataAfter = new HashMap<>();
						var1.forEach((k1,v1)->{
							if(String.class.isInstance(v1))
								jsondataAfter.put(k1, Utils.checkGetAll((String)v1, bindmap));
							else {
								jsondataAfter.put(k1, v1);
							}
						});
						bindresult.put(k,JSONObject.toJSONString(jsondataAfter));
				}
			});
		}
		return bindresult;

	}


	@Test(dataProvider ="data")
	public void test(StepMeta step){
		System.out.println(step);

		Response response = null;
    	StepDetail stepDetail =step.getStepDetail();
    	RequestMeta request=stepDetail.getRequest();
    	RequestSpecification RS = given();
    	//处理requestbinds
    	if(handleRequestBind(step.getStepDetail(),bindmap) != null)
    		bindmap.putAll(handleRequestBind(step.getStepDetail(),bindmap));
    	//form
    	HashMap<String, String> var = request.getFormdata();
    	if(var!=null){
        	var.forEach((k,v)->{
					RS.param(k, Utils.checkGetAll(v, bindmap));
    		});
    	}
    	//json
    	HashMap<String, Object> jsonvar = request.getJsondata();
    	if(jsonvar!=null){
    		jsonvar.forEach((k,v)->{
    			if(String.class.isInstance(v)){
    				jsonvar.put(k, Utils.checkGetAll((String) v, bindmap));
    			}
    		});
    		String jsonvarbody = JSONObject.toJSONString(jsonvar);
    		System.out.println("body is :"+jsonvarbody);
//    		RS.body(EncrptUtil.encrpt(JSONObject.toJSONString(jsonvar), password));

    		//处理requesthandler
        	HashMap<String,String> requesthandler = stepDetail.getRequesthandler();
        	if(requesthandler!=null){
        		requesthandler.forEach((k,v)->{
            	    Object body = Utils.checkGetAll(v, bindmap);
            	    System.out.println("bindmap is :"+bindmap);
            	    if(k.matches("^body$")) {

            	    	RS.body(body);
            	    }
        		});
        	}
    	}

    	//header
    	HashMap<String, String> var1= request.getHeaders();
    	if(var1!=null){
    	var1.forEach((k,v)->{
    			RS.header(k, Utils.checkGetAll(v, bindmap));
		});
    	}
    	//Cookie
    	HashMap<String, String> var2= request.getCookie();
    	if(var2!=null){
        	var2.forEach((k,v)->{
        			RS.cookie(k,Utils.checkGetAll(v, bindmap));
    		});
    	}


    	//URL
    	switch (step.getStepDetail().getRequest().getMethod().toLowerCase()) {
			case "post":
				response = RS.post(step.getStepDetail().getRequest().getUrl());
				break;
			case "get":
				response = RS.get(step.getStepDetail().getRequest().getUrl());
				break;

			default:
				break;
		}
    	//先绑定，处理responsehandler可能会用到
    	if(handleResponseBind(step.getStepDetail(), response,bindmap) != null)
    		bindmap.putAll(handleResponseBind(step.getStepDetail(), response,bindmap));

    	//处理responsehandler
    	HashMap sHashMap = JSONObject.parseObject(response.asString(), HashMap.class);
    	HashMap<String,String> responsehandler = stepDetail.getResponsehandler();
    	if(responsehandler!=null){
    		responsehandler.forEach((k,v)->{
        		sHashMap.put(k, Utils.checkGetAll(v, bindmap));
    		});
    	}


		System.out.println("解密后的数据："+sHashMap);



    	//校验点
    	ArrayList<ValidateMeta> var3list = stepDetail.getValidate();
    	for(ValidateMeta var3:var3list){
    		if(var3.getCheck().matches("^body\\.(.*)")){
    			if(var3.getComparator().equals("eq")){
    				String expected = JsonPath.from(JSONObject.toJSONString(sHashMap)).getString(Utils.getJsonPath(var3.getCheck()));
    					Assert.assertEquals( expected,var3.getExpect());
//    				if(var3.getExpect().equals(expected)){
//    					System.out.println(var3.getExpect()+":check Ok");
//    				}
    			}
    		}
    	}

	}

	@DataProvider(name="data")
	public Iterator<StepMeta> dataprovider() throws FileNotFoundException{
		Yaml yaml = new Yaml();
//      URL url = Test.class.getClassLoader().getResource("test.yaml");

        //获取test.yaml文件中的配置数据，然后转换为obj，
        InputStream in = new FileInputStream(ResourceUtils.getFile("classpath:test3.yaml"));
//        System.out.println(((ArrayList<Object>)yaml.load(in)).get(0).toString());
        CaseMeta teStepMetas= yaml.loadAs(in, CaseMeta.class);
        return teStepMetas.getCasedata().iterator();
	}





}
