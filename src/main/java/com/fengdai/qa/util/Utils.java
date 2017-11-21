package com.fengdai.qa.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;



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
	
	  
	
	
	
	public static Object checkGetfunc(String funvar,HashMap<String, Object> bindmap) throws Exception{
		String function_regexp = "^\\$\\{(\\w+)\\(([\\w,$.]*)\\)\\}$";
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
	          String funvars= m.group(2);
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
				throw new Exception("zhuan huan shibai");
		}
		return var;  
	}
	
	public static Object checkGetAll(String var,HashMap<String, Object> bindmap)  {
		
		String variable_regexp = "^\\$(\\w+)";
		String function_regexp = "^\\$\\{(\\w+)\\(([\\w,$.]*)\\)\\}$";
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

		String function_regexp = "^\\$\\{(\\w+)\\(([\\w,$.]*)\\)\\}$";

//		String function_regexp_fun = "^\\$\\{(\\w+)\\((.*)\\)\\}$";

		String var = "$a";
//		String funvar="${func(10,$a)}";
		String funvar="${getphone(18667906998)}";
//		String funvarfun="${func($func(6,10), 5a,10b#)}";
		HashMap<String, Object> bindmap = new HashMap<>();
		bindmap.put("a", "123456");
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


//		// 按指定模式在字符串查找
//	      String line = "This order was placed for QT3000! OK?";
//	      String pattern = "(\\D*)(\\d+)(.*)";
//
//	      // 创建 Pattern 对象
//	      Pattern r = Pattern.compile(function_regexp);
//	      // 现在创建 matcher 对象
//	      Matcher m = r.matcher(funvar);
//	      if (m.find( )) {
//	    	  System.out.println(m.groupCount());
//	          System.out.println("Found value: " + m.group(0) );
//	          System.out.println("Found value: " + m.group(1) );
//	          System.out.println("Found value: " + m.group(2) );
//	          String funvars= m.group(2);
//	          String vars[] = funvars.split(",");
//	          for(int i=0;i<vars.length;i++){
//	        	  if(Pattern.matches(variable_regexp, vars[i])){
//	        		  vars[i]="1111";
//	        	  }
//	          }
//	          Utils test = new Utils();
//	          Method mt = Utils.class.getMethod("func", String.class,String.class);
//	          Object a=   mt.invoke(test, vars[0],vars[1]);
//	          System.out.println((Integer)a);
//	       } else {
//	          System.out.println("NO MATCH");
//	       }

	}

}
