package com.fengdai.qa.testcase;

import com.alibaba.fastjson.JSONObject;

public class TestCompany {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		
		String var="com.fengdai.qa.meta.LoginMeta";
		String temp="{\"loginMethod\":\"post\",\"mobile\":\"18667906998\"}";
		System.out.println(temp);
		Class var1 = Class.forName(var);
		Object o= var1.newInstance();
		System.out.println(o.getClass());
		Object object = JSONObject.parseObject(temp, o.getClass());
		System.out.println(object.getClass());
		System.out.println(JSONObject.toJSONString(JSONObject.parseObject(temp, o.getClass())));

	}

}
