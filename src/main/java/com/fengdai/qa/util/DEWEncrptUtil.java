package com.fengdai.qa.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.fengdai.qa.meta.LoginMeta;

public class DEWEncrptUtil {

	public static String encrpt(String oldData,String password){
		
		JSONObject jsonObject = JSONObject.parseObject(oldData);
		String content = jsonObject.getString("content");
		jsonObject.put("content", DEWAesUtil.aesEncrypt(content, password));
		Map<String, Object> map = jsonObject.toJavaObject(Map.class);
		// 获取所有key
		Set<String> set = map.keySet();
		// 转换成数组
		String[] keys = new String[set.size()];
		set.toArray(keys);
		// 排序所有的key
		Arrays.sort(keys);
		// 所有参数
		StringBuilder params = new StringBuilder();
		// 开始拼接参数
		for (String key : keys) {
			// 手动过滤掉sign参数
			if (!"sign".equals(key) && !"parameter".equals(key) && !"data".equals(key)) {
				params.append("&").append(key).append("=").append(map.get(key));
			}
		}
		String sign = DEWMD5Util.MD5(params.substring(1));// 系统签名
		jsonObject.put("sign", sign);
//		System.out.println(jsonObject.toJSONString());
		return jsonObject.toJSONString();
		
		
	}
	
	
	public static void main(String[] args) {
//		String oldData="{\"code\":\"1000\","
//				+ "\"content\":{\"parameter\":{\"pid\":\"0\"}},"
//				+ "\"time\":\"1511076995592\","
//				+ "\"type\":\"android\","
//				+ "\"device_id\":\"ios01\","
//				+ "\"token\":\"token01\"}";
		
		
		
		System.out.println(DEWAesUtil.aesDecrypt("/rpF2brX65nfprNaT1n97QGR/2vRarq/x7xRj1FAFjtIp1oCWHbLJf5Rd5EB ue3ADoD/Oo6/gDQhyv5ffL7IQ0EKWLnrilUdil0vHPcHt4/HJl2QimXf34nc CHuYDgtA2YCr4N/EEt+BHN9mBOUBHZ1KpOjL3bWrHNIgiPRz13Su6SpktZU6 O7aAlb5fhdJ+LGmPRbbdhGbN6YRCzVr0yw__",
				""));
		
	}

}
