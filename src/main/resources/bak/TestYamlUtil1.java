package com.fengdai.qa.util;
import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSONObject;
import com.fengdai.qa.meta.CaseMeta;
import com.fengdai.qa.meta.RequestMeta;
import com.fengdai.qa.meta.StepDetail;
import com.fengdai.qa.meta.StepMeta;
import com.fengdai.qa.meta.ValidateMeta;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.path.json.exception.JsonPathException;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;



public class TestYamlUtil1 {

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

	
	
	
	
	
	
    public static void main(String[] args) throws Exception{
//    	RestAssured.baseURI = "http://opapi.dev.e-dewin.com";
    	RestAssured.baseURI = "http://opapi.dev.e-dewin.com";
        HashMap<String, Object> bindmap= new HashMap<>();


            Yaml yaml = new Yaml();
//          URL url = Test.class.getClassLoader().getResource("test.yaml");

            //获取test.yaml文件中的配置数据，然后转换为obj，
            InputStream in = new FileInputStream(ResourceUtils.getFile("classpath:test1.yaml"));
//            System.out.println(((ArrayList<Object>)yaml.load(in)).get(0).toString());
            CaseMeta teStepMetas= yaml.loadAs(in, CaseMeta.class);

            for(StepMeta step:teStepMetas.getCasedata()){
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
//            		RS.body(EncrptUtil.encrpt(JSONObject.toJSONString(jsonvar), password));

            		//处理requesthandler
                	HashMap<String,String> requesthandler = stepDetail.getRequesthandler();
                	if(requesthandler!=null){
                		requesthandler.forEach((k,v)->{
    	            	    Object body = Utils.checkGetAll(v, bindmap);
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
            				if(var3.getExpect().equals(JsonPath.from(JSONObject.toJSONString(sHashMap)).getString(Utils.getJsonPath(var3.getCheck())))){
            					System.out.println(var3.getExpect()+":check Ok");
            				}
            			}
            		}
            	}





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





    }

}