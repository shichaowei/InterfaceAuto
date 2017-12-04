package com.fengdai.qa.har;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSONObject;
import com.fengdai.qa.meta.CaseMeta;
import com.fengdai.qa.meta.RequestMeta;
import com.fengdai.qa.meta.StepDetail;
import com.fengdai.qa.meta.ValidateMeta;
import com.google.gson.JsonSyntaxException;
import com.sangupta.har.HarUtils;
import com.sangupta.har.model.Har;
import com.sangupta.har.model.HarEntry;
import com.sangupta.har.model.HarHeader;
import com.sangupta.har.model.HarPostParam;
import com.sangupta.har.model.HarRequest;
import com.sangupta.har.model.HarResponse;

public class harUtil {
	
	public static void main(String[] args) throws JsonSyntaxException, FileNotFoundException, IOException {
		generateYaml("koudailoan");
	}

	@SuppressWarnings("unchecked")
	public static void generateYaml(String harname) throws JsonSyntaxException, FileNotFoundException, IOException {
		Har har = HarUtils.read(ResourceUtils.getFile("classpath:"+harname+".har"));
		CaseMeta caseMeta = new CaseMeta();
		ArrayList<StepDetail> steps = new ArrayList<>();
		for(HarEntry entry:har.log.entries){
			//把stepDetai给赋值
			StepDetail stepDetail = new StepDetail();
			HarRequest request = entry.request;
			HarResponse response = entry.response;
			
			
			ArrayList<ValidateMeta> validateMetas = new ArrayList<ValidateMeta>();
			RequestMeta requestMeta = new RequestMeta();
			
			//构造request
			
			//request
			String contentType = "";
			System.out.println("url: "+entry.request.url);
			requestMeta.setUrl(request.url);
			System.out.println("method: "+entry.request.method);
			requestMeta.setMethod(request.method);
			HashMap<String, String> headerMap = new HashMap<>();
			for(HarHeader header: request.headers){
				headerMap.put(header.name, header.value);
//				System.out.println("hearder{ "+header.name+":"+header.value+"}");
				if(header.name.equals("Content-Type")){
					contentType=header.value;
				}
			}
			
			requestMeta.setHeaders(headerMap);
			if(entry.request.method.toLowerCase().equals("get")){
				System.out.println("queryString: "+request.queryString);
//				requestMeta.setParams();
				
			}
		
			
			else if (entry.request.method.toLowerCase().equals("post")&&contentType.contains("application/x-www-form-urlencoded")) {
				HashMap<String, Object> params = new HashMap<>();
				for (HarPostParam param:request.postData.params) {
					System.out.println(param.name+":"+param.value);
					params.put(param.name, param.value);
				}
				requestMeta.setParams(params);
			}
			else if (entry.request.method.toLowerCase().equals("post")&&contentType.contains("application/json")) {
				System.out.println("postjsonData: "+request.postData.text);
				requestMeta.setJsondata(JSONObject.parseObject(request.postData.text, HashMap.class));
			}
			else{
//				System.out.println("methodd"+request.method);
//				System.out.println("textdd"+request.postData.text);
				throw new RuntimeException("只支持get post 及form json表单提交");
			}
			//构造校验
			
			System.out.println("response mimetype:"+response.content.mimeType);
			ValidateMeta v1=new ValidateMeta();
			v1.setCheck("statusCode");
			v1.setComparator("eq");
			v1.setExpect(Integer.toString(response.status));
			validateMetas.add(v1);
//			System.out.println("response stauts:"+response.status);
			//如果request的内容类型为空 不计入用例 
			if(response.content.mimeType == null){
				stepDetail.setName(request.url);
				stepDetail.setRequest(requestMeta);
				stepDetail.setValidate(validateMetas);
				steps.add(stepDetail);
			}else
			if(response.content.mimeType.toLowerCase().contains("application/json")){
				System.out.println("response json:"+response.content.text);
//				HashMap<String,Object> var = JSONObject.parseObject(response.content.text,HashMap.class);
//				var.forEach((k1,v1)->{
//					validateMetas.add(new ValidateMeta().setCheck("body."+k1)
//							.setComparator("eq").setExpect(String.valueOf(v1)));
//					
//				});
				
			stepDetail.setName(request.url);
			stepDetail.setRequest(requestMeta);
			stepDetail.setValidate(validateMetas);
			steps.add(stepDetail);
			}
			caseMeta.setCasedata(steps);
		}
		/* Export data to a YAML file. */
        File dumpFile = new File(System.getProperty("user.dir") + "/src/main/resources/"+harname+".yaml");
        Writer output = new FileWriter(dumpFile);
        new Yaml().dump(caseMeta, output);
		
		
	}

}