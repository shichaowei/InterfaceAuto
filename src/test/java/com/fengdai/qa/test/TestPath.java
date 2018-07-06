package com.fengdai.qa.test;
import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import  io.restassured.path.json.JsonPath;

import scala.Array;

/**
 * 此方法目的验证json修改
 * 路径使用gpath语法
 * value为要修改的字符串，也可以是javabean 只需要转成json就行
 *
 */

public class TestPath {



    public  static HashMap<String,String> getpathmap(String path){
        HashMap<String,String> result = new HashMap<>();
        String hasindex_regexp = "(.*)\\[(.*)\\]";
//        String noindex_regexp = "(.*)\\[(.*)\\]";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(hasindex_regexp);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(path);
        if (m.find( )) {
            System.out.println(m.group(1));
            System.out.println(m.group(2));
            result.put("key",m.group(1));
            result.put("index",m.group(2));
        }else {
            result.put("key",path);
            result.put("index",null);
        }

        return  result;
    }



//    public static  void processabc(Object json,ArrayList<String> paths,String value){
//        boolean jsonObjFlag= true;
//
//        if(paths.size()==1){
//            HashMap<String,String> pathmap = getpathmap(paths.get(0));
//            if(pathmap.get("key")!=null&& jsonObjFlag){
//
//                if(com.alibaba.fastjson.JSONObject.class.isInstance(jsonObject.get(pathmap.get("key")))){
//                    jsonObject = jsonObject.getJSONObject(pathmap.get("key"));
//                    jsonObjFlag=true;
//                }else {
//                    jsonArray = jsonObject.getJSONArray(pathmap.get("key"));
//                    jsonObjFlag=false;
//                }
//            }else if(pathmap.get("key")!=null&& !jsonObjFlag){
//
//            }
//
//        }
//
//
//
//
//    }




	public static void main(String[] args) {



//		String var = given().param("code", "FDWMDSD001").when().get("http://www.360fengdai.com/loanDetail").asString();
//		System.out.println(var);
//		String var = given().param("phone", "18667906998").param("password", "wsc6821051").
//		post("http://www.360fengdai.com/account/user/login").getBody().asString();
//		String var ="{list:[{id:1,name:wsc}{id:2,name:wsk}]}";
		String jsonString ="{\"mmm\":[{\"id\":1,\"name\":\"wsc\"},{\"id\":2,\"name\":\"wsk\"}]}";
//		String jsonString ="{\"id\":1,\"name\":[{\"id\":1,\"name\":\"wsc\"},{\"id\":2,\"name\":\"wsk\"}]}";
//		Object vString= JsonPath.read(var,"$[0]");
//		System.out.println(vString);
//		JSONObject.toJSONString(var);
//		String var ="{\"phone\":\"18667906998\",\"grantCode\":\"7a7872\"}";
//		System.out.println(var);
        System.out.println("修改前的："+jsonString);
		Object temp = JsonPath.from(jsonString).get("mmm[0].name");

        System.out.println(temp);

		String[] paths = "mmm[0].name".split("\\.");
//		Object varmm =object.get("mmm");
//        System.out.println(JSONArray.class.isInstance(varmm));

        Object objecttemp = null;

        try {
            objecttemp = JSONObject.parseObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            objecttemp =JSONObject.parseArray(jsonString);
        }



        Object object=objecttemp;
        String changeStr ="abc";
		for(int i =0;i<paths.length;i++){



                String path= paths[i];
                HashMap<String,String> pathmap = getpathmap(path);

                if(pathmap.get("key")!=null) {
                    if(JSONObject.class.isInstance(object)) {
                        if(i==paths.length-1){
                            ((JSONObject) object).put(pathmap.get("key"),changeStr);
                        }else {
                            if (JSONObject.class.isInstance(((JSONObject) object).get(pathmap.get("key")))) {
                                object = ((JSONObject) object).getJSONObject(pathmap.get("key"));
                            } else if (JSONArray.class.isInstance(((JSONObject) object).get(pathmap.get("key")))) {
                                object = ((JSONObject) object).getJSONArray(pathmap.get("key"));
                            } else {
                                System.out.println("找到了一个叶子节点");
                                System.out.println(((JSONObject) object).get(pathmap.get("key")));
                                if (i == paths.length - 1) {
//                                ((JSONObject) object).put(pathmap.get("key"),changeStr);
                                } else {
                                    throw new RuntimeException("代码有毒" + jsonString);
                                }
                            }
                        }
                    }else {
                        throw  new RuntimeException("代码有毒"+jsonString);
                    }
                }

                if(pathmap.get("index")!=null){
                    if(JSONArray.class.isInstance(object)) {
                        if(i==paths.length-1){
                            ((JSONArray) object).set(Integer.valueOf(pathmap.get("index")),changeStr);
                            break;
                        }
                        if (JSONObject.class.isInstance(((JSONArray) object).get(Integer.valueOf(pathmap.get("index"))))) {
                            object = ((JSONArray) object).getJSONObject(Integer.valueOf(pathmap.get("index")));
                        } else {
                            object = ((JSONArray) object).getJSONArray(Integer.valueOf(pathmap.get("index")));
                        }

                    }else {
                        throw  new RuntimeException("代码有毒"+jsonString);
                    }
                }









        }

        System.out.println("修改后的"+objecttemp);



        JSONObject object1 = JSONObject.parseObject(jsonString);
        System.out.println(object1.get("mmm").getClass());
        if(JSONArray.class.isInstance(object1.get("mmm"))){
            System.out.println(object1.getJSONArray("mmm").getJSONObject(0).get("name"));
            Object var = object1.getJSONArray("mmm");
            Object var1=((JSONArray)var).getJSONObject(0);
            ((JSONObject)var1).put("name","mnq");
//            object1.getJSONArray("mmm").set(0,"sfdfdfd");
        }

        System.out.println(object1.toJSONString());

		
		
	}
}
