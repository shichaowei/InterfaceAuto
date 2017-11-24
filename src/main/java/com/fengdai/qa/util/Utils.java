package com.fengdai.qa.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;



public class Utils {

	public static int func(String a,String b) {
		return Integer.valueOf(a)+Integer.valueOf(b);
	}

	public static String getphone(String a) {
		return a;
	}

	public static long getcurrenttime() {
		return System.currentTimeMillis();
	}

	//加密解密
	public static HashMap<String, String> getmingwen(String content,String password) {
//		System.out.println("content:"+AesUtil.aesDecrypt(content, password));
		HashMap<String, String> result = new HashMap<>();
		try {
			result = JSONObject.parseObject(AesUtil.aesDecrypt(content, password),HashMap.class);
		} catch (Exception e) {
			return null;
		}
		return result;
	}
	

	public static String handlerdewein(String body,String password) {
		return EncrptUtil.encrpt(body, password);
	}

	@SuppressWarnings("unchecked")
	public static void handleMapdata(HashMap<String, Object> var1,HashMap<String, Object> bindmap) {
		var1.forEach((k1,v1)->{
			if(String.class.isInstance(v1)) {
				var1.put(k1, Utils.checkGetAll((String)v1, bindmap));
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
            	v1.add( Utils.checkGetAll((String)listvar, bindmap));
            }else if (ArrayList.class.isInstance(listvar)) {
				handleListdata((ArrayList<Object>) listvar, bindmap);
			}else if (HashMap.class.isInstance(listvar)) {
				handleMapdata((HashMap<String, Object>) listvar, bindmap);
			}
        });
	}
	
	
	public static Object handleObject(Object var,HashMap<String, Object> bindmap) {
		Object result=null;
		if(String.class.isInstance(var)){
        	result = Utils.checkGetAll((String)var, bindmap);
        }else if (ArrayList.class.isInstance(var)) {
			handleListdata((ArrayList<Object>) var, bindmap);
		}else if (HashMap.class.isInstance(var)) {
			handleMapdata((HashMap<String, Object>) var, bindmap);
		}
		return result;
	}




	public static Object checkGetfunc(String funvar,HashMap<String, Object> bindmap) throws Exception{
//		String function_regexp = "^\\$\\{(\\w+)\\(([\\w,$.]*)\\)\\}$";
//		String function_regexp = "^\\$\\{(\\w+)\\(([\\w,$.\"]*)\\)\\}$";
		String function_regexp = "^\\$\\{(\\w+)\\((.*)\\)\\}$";
		String variable_regexp = "^\\$(\\w+)";
        Object result = null;
		// 创建 Pattern 对象
	      Pattern r = Pattern.compile(function_regexp);
	      // 现在创建 matcher 对象
	      Matcher m = r.matcher(funvar);
	      if (m.find( )) {
//	    	  System.out.println(m.groupCount());
//	          System.out.println("Found value: " + m.group(0) );
//	          System.out.println("Found value: " + m.group(1) );
//	          System.out.println("Found value: " + m.group(2) );
	          String funvars= m.group(2).replace("\"", "");
	          String vars[] = funvars.split(",");

	          //验证参数是否是$a
	          for(int i=0;i<vars.length;i++){
	        	  Pattern rp = Pattern.compile(variable_regexp);
        	      Matcher matcher = rp.matcher(vars[i]);
	        	  if(matcher.find()){
	        		  	if(bindmap.get(matcher.group(1)) != null)
	        	    	  vars[i]=(String) bindmap.get(matcher.group(1));
	        		  	else
	        		  		throw new Exception("zhuan huan shibai");
	        	  }
	          }

	          Utils test = new Utils();
	          Method[] mts = Utils.class.getMethods();
	          for(Method mt:mts){
//	        	  System.out.println(m.group(1));
//	        	  System.out.println(mt.getName());
//	        	  System.out.println(mt.getParameterTypes().length);
//	        	  System.out.println(vars.length);
	        	  if(mt.getName().equals(m.group(1))){
	        		  if(mt.getParameterTypes().length == vars.length){
	        			  result =   mt.invoke(test, vars);
	        			  break;
	        		  }
	        		  else if (vars.length == 1 && StringUtils.isEmpty(vars[0]) &&mt.getParameterTypes().length==0) {
	        			  result =   mt.invoke(test);
	        			  break;
					}
	        	  }
	          }

	          return result;
	       } else {
	          return funvar;
	       }
	}

	public static String getJsonPath(String checkstr){
		String var="";
		String variable_regexp = "^body\\.(.*)";
		Pattern rp = Pattern.compile(variable_regexp);
		Matcher matcher = rp.matcher(checkstr);
		if(matcher.find()){
			var= matcher.group(1);
//			System.out.println(matcher.group(1));
		}
		return var;
	}

	public static String checkGetkvar(String var,HashMap<String, Object> bindmap) throws Exception {
		String variable_regexp = "^\\$(\\w+)";
		Pattern rp = Pattern.compile(variable_regexp);
		Matcher matcher = rp.matcher(var);
		if(matcher.find()){
			if(bindmap.get(matcher.group(1)) != null){
				  var=(String) bindmap.get(matcher.group(1));
			}
			else
				throw new Exception(var+"*****转换失败");
		}
		return var;
	}

	public static Object checkGetAll(String var,HashMap<String, Object> bindmap)  {

		String variable_regexp = "^\\$(\\w+)";
		String function_regexp = "^\\$\\{(\\w+)\\((.*)\\)\\}$";
//		String function_regexp = "^\\$\\{(\\w+)\\(([\\w,$.]*)\\)\\}$";
//		String function_regexp = "^\\$\\{(\\w+)\\(([\\w,$.\"]*)\\)\\}$";
		Pattern rp = Pattern.compile(variable_regexp);
		Matcher matcher = rp.matcher(var);
		if(matcher.find()){
			try {
				return checkGetkvar(var, bindmap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 创建 Pattern 对象
		Pattern r = Pattern.compile(function_regexp);
	    // 现在创建 matcher 对象
	    Matcher m = r.matcher(var);
	    if (m.find( )) {
	    	try {
				return checkGetfunc(var, bindmap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		return var;

	}


	public static void main(String[] args) throws Exception {

		String variable_regexp = "^\\$(\\w+)";

		String function_regexp = "^\\$\\{(\\w+)\\((.*)\\)\\}$";

//		String function_regexp_fun = "^\\$\\{(\\w+)\\((.*)\\)\\}$";

		String var = "$a";
//		String funvar="${func($absd,\"sfsdfds\")}";
//		String funvar="${getphone(18667906998)}";
		String funvar="${getmingwen($content,\"DWERP@#12$3458ta\")}";
//		String funvarfun="${func($func(6,10), 5a,10b#)}";
		HashMap<String, Object> bindmap = new HashMap<>();
		bindmap.put("content", "123456");
		System.out.println(checkGetAll(funvar, bindmap));
		getJsonPath("body.content.data.userInfo.contactPhone");

		/**
		// 用于定义正规表达式对象模板类型
        PatternCompiler compiler = new Perl5Compiler();

        // 正规表达式比较批配对象
        PatternMatcher matcher = new Perl5Matcher();

        // 实例大小大小写敏感的正规表达式模板
        Pattern hardPattern = compiler.compile(variable_regexp);
        if(matcher.contains(var, hardPattern)) {
        	// 返回批配结果
        	System.out.println(matcher.getMatch().group(1));
        }
		 **/

/*
		// 按指定模式在字符串查找
	      String line = "This order was placed for QT3000! OK?";
	      String pattern = "(\\D*)(\\d+)(.*)";

	      // 创建 Pattern 对象
	      Pattern r = Pattern.compile(function_regexp);
	      // 现在创建 matcher 对象
	      Matcher m = r.matcher(funvar);
	      if (m.find( )) {
	    	  System.out.println(m.groupCount());
	          System.out.println("Found value: " + m.group(0) );
	          System.out.println("Found value: " + m.group(1) );
	          System.out.println("Found value: " + m.group(2) );
	          String funvars= m.group(2);
	          String vars[] = funvars.split(",");
	          for(int i=0;i<vars.length;i++){
	        	  if(Pattern.matches(variable_regexp, vars[i])){
	        		  vars[i]="1111";
	        	  }
	          }
	          Utils test = new Utils();
	          Method mt = Utils.class.getMethod("func", String.class,String.class);
	          Object a=   mt.invoke(test, vars[0],vars[1]);
	          System.out.println(a);
	       } else {
	          System.out.println("NO MATCH");
	       }
*/
	}

}
