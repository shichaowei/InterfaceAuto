package com.fengdai.qa.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSONObject;
import com.fengdai.qa.meta.CaseMeta;
import com.fengdai.qa.util.CaseUtil;

import io.restassured.path.json.JsonPath;

public class TestStr {
	
	
	@SuppressWarnings("unchecked")
	public static void handleMapdata(HashMap<String, Object> var1,HashMap<String, Object> bindmap) {
		var1.forEach((k1,v1)->{
			System.out.println(k1+"----"+v1);
			if(String.class.isInstance(v1)) {
				var1.put(k1, CaseUtil.checkGetAll((String)v1, bindmap));
			}
			else {
				if(ArrayList.class.isInstance(v1)){
					 handleListdata((ArrayList<Object>) v1, bindmap);
				}else if (HashMap.class.isInstance(v1)) {
					handleMapdata((HashMap<String, Object>) v1, bindmap);
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void handleListdata(ArrayList<Object> v1,HashMap<String, Object> bindmap) {
		((ArrayList<Object>) v1).stream().forEach(listvar ->{  
            if(String.class.isInstance(listvar)){
            	v1.remove(listvar);
            	v1.add( CaseUtil.checkGetAll((String)listvar, bindmap));
            }else if (ArrayList.class.isInstance(listvar)) {
				handleListdata((ArrayList<Object>) listvar, bindmap);
			}else if (HashMap.class.isInstance(listvar)) {
				handleMapdata((HashMap<String, Object>) listvar, bindmap);
			}
        });
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		HashMap<String, Object> bindmap = new HashMap<>();
		bindmap.put("userId", "123user");
		bindmap.put("busareaId", "12522bus");
		bindmap.put("token", "12522token");
		String var = "{code:13007, device_id:cb6685f3-4370-3664-8dbb-41655ff55644, time:12221222, type:android, content:{parameter:{userId:$userId, busareaId:$busareaId}}}";
		System.out.println(JSONObject.toJSONString(var));
		
		Yaml yaml = new Yaml();
//      URL url = Test.class.getClassLoader().getResource("test.yaml");

        //获取test.yaml文件中的配置数据，然后转换为obj，
        InputStream in = new FileInputStream(ResourceUtils.getFile("classpath:test3.yaml"));
//        System.out.println(((ArrayList<Object>)yaml.load(in)).get(0).toString());
        CaseMeta teStepMetas= yaml.loadAs(in, CaseMeta.class);
        HashMap<String, Object> var1 = teStepMetas.getCasedata().get(1).getRequest().getJsondata();
		System.out.println(var1);
//		HashMap<String, Object> var1 = JSONObject.parseObject(JSONObject.toJSONString(var), HashMap.class);
		handleMapdata(var1, bindmap);
		System.out.println(var1);
	}

}
