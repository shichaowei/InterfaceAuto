package com.fengdai.qa.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.ast.VariableDeclaration;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSONObject;
import com.fengdai.qa.meta.CaseMeta;
import com.fengdai.qa.meta.RequestMeta;
import com.fengdai.qa.meta.StepDetail;
import com.fengdai.qa.meta.StepMeta;
import com.fengdai.qa.meta.ValidateMeta;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.corba.se.impl.javax.rmi.CORBA.Util;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;

import groovy.util.IFileNameFinder;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;



public class TestYamlUtil1 {

	/**
	 * k值为要查找的value的路径
	 * @param stepDetail
	 * @param response
	 * @return
	 */
	public static HashMap<String, String> getbindvar (StepDetail stepDetail,Response response) {
		HashMap<String, String> bindresult = new HashMap<>();
		HashMap<String, String> var= stepDetail.getExtractors();
		if(var !=null){
		var.forEach((k,v)->{
			if(v.matches("^cookie.*")){
				bindresult.put(k, response.getCookie(k));
			}else if (v.matches("^content.*")) {
				try {
					bindresult.put(k, response.getBody().path(k));
				} catch (JsonPathException e) {
					System.out.println("response is :"+response.getBody());
					e.printStackTrace();
				}
			}
		});
		}
		return bindresult;

	}
	
    public static void main(String[] args) throws Exception{
    	RestAssured.baseURI = "http://****.dev.e-dewin.com";
    	String password="*********";
        HashMap<String, Object> bindmap= new HashMap<>();
        try {
        	
            Yaml yaml = new Yaml();
//            URL url = Test.class.getClassLoader().getResource("test.yaml");
            
                //获取test.yaml文件中的配置数据，然后转换为obj，
            InputStream in = new FileInputStream(ResourceUtils.getFile("classpath:test1.yaml"));
//            System.out.println(((ArrayList<Object>)yaml.load(in)).get(0).toString());
//            ArrayList<StepMeta> teStepMetas = new ArrayList<StepMeta>();
            CaseMeta teStepMetas= yaml.loadAs(in, CaseMeta.class);
            System.out.println(teStepMetas.getCasedata().get(0).getStepDetail().getName());
            
            for(StepMeta step:teStepMetas.getCasedata()){
            	Response response = null;
            	StepDetail stepDetail =step.getStepDetail();
            	RequestMeta request=stepDetail.getRequest();
            	RequestSpecification RS = given();
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
//            			System.out.println(v+":"+v.getClass());
            			if(String.class.isInstance(v)){
            				jsonvar.put(k, Utils.checkGetAll((String) v, bindmap));
            			}
            		});
            	}
//            	System.out.println("jsondata:"+JSONObject.toJSONString(jsonvar));
            	RS.body(EncrptUtil.encrpt(JSONObject.toJSONString(jsonvar), password));
            	
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
//            	System.out.println(response.asString());
            	//解密
            	HashMap sHashMap = JSONObject.parseObject(response.asString(), HashMap.class);
            	String content =response.path("content");
//        		System.out.println(sHashMap);
//        		System.out.println("揭秘后的数据："+AesUtil.aesDecrypt(content, password));
        		HashMap contentmap = JSONObject.parseObject(AesUtil.aesDecrypt(content, password), HashMap.class);
        		sHashMap.put("content", contentmap);
        		System.out.println(sHashMap);
//        		System.out.println(JsonPath.from(JSONObject.toJSONString(sHashMap)).getString("content.data.userInfo.contactPhone"));
        		
        		
            	
            	//校验点
            	ArrayList<ValidateMeta> var3list = stepDetail.getValidate();
            	for(ValidateMeta var3:var3list){
            		System.out.println(var3.getCheck());
            		System.out.println(var3.getExpect());
            		if(var3.getCheck().matches("^body\\.(.*)")){
            			if(var3.getComparator().equals("eq")){
            				if(var3.getExpect().equals(JsonPath.from(JSONObject.toJSONString(sHashMap)).
            						getString(Utils.getJsonPath(var3.getCheck())))){
            					System.out.println(var3.getExpect()+":check Ok");
            				}
            			}
            		}
            	}
            	
            	if(getbindvar(step.getStepDetail(), response) != null)
            		bindmap.putAll(getbindvar(step.getStepDetail(), response));
            	
            	
            	
            }
//            for(StepMeta var : teStepMetas){
//            	System.out.println(var);
//            }
            
            
//            String temp = yaml.dump(yaml.load(in));
//            System.out.println(temp);
//            JsonPath.from(temp).get("test[0].request");
                //也可以将值转换为Map
//                Map map =(Map)yaml.loads(ResourceUtils.getFile("classpath:test.yaml"));
//                System.out.println(map.get("test"));
                //通过map我们取值就可以了.
                
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }

}