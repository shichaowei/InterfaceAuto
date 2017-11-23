package com.fengdai.qa.util;
import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import com.fengdai.qa.meta.CaseMeta;
import com.fengdai.qa.meta.RequestMeta;
import com.fengdai.qa.meta.StepDetail;
import com.fengdai.qa.meta.StepMeta;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;



public class YamlUtil {

	public static HashMap<String, String> getbindvar (StepDetail stepDetail,Response response) {
		HashMap<String, String> bindresult = new HashMap<>();
		HashMap<String, String> var= stepDetail.getResponsebind();
		if(var !=null){
		var.forEach((k,v)->{
			if(v.matches("^cookie.*")){
				bindresult.put(k, response.getCookie(k));
			}
		});
		}
		return bindresult;

	}

    public static void main(String[] args) throws Exception{
    	RestAssured.baseURI = "http://10.200.141.34:81/";
        HashMap<String, Object> bindmap= new HashMap<>();
        try {

            Yaml yaml = new Yaml();
//            URL url = Test.class.getClassLoader().getResource("test.yaml");

                //获取test.yaml文件中的配置数据，然后转换为obj，
            InputStream in = new FileInputStream(ResourceUtils.getFile("classpath:test.yaml"));
//            System.out.println(((ArrayList<Object>)yaml.load(in)).get(0).toString());
//            ArrayList<StepMeta> teStepMetas = new ArrayList<StepMeta>();
            CaseMeta teStepMetas= yaml.loadAs(in, CaseMeta.class);
            System.out.println(teStepMetas.getCasedata().get(0).getStepDetail().getName());

            for(StepMeta step:teStepMetas.getCasedata()){
            	Response response = null;
            	StepDetail stepDetail =step.getStepDetail();
            	RequestMeta request=stepDetail.getRequest();
            	RequestSpecification RS = given();
            	//formdata
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
            		RS.body(jsonvar);
            	}

            	//header
            	HashMap<String, String> var1= request.getHeaders();
            	if(var1!=null){
            	var1.forEach((k,v)->{
            			RS.header(k, v);
        		});
            	}
            	//Cookie
            	HashMap<String, String> var2= request.getCookie();
            	if(var2!=null){
            	var2.forEach((k,v)->{
            		//test
            		if(v.matches(".*\\$.*")){
            			String token = v.replace("$token", (String)bindmap.get("token"));
            			RS.cookie(k,token);
            		}else{
            			RS.cookie(k, v);
            		}
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
            	System.out.println(response.asString());
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