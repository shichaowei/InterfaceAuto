package com.fengdai.qa.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.oro.text.regex.MalformedPatternException;


public class Utils {


	public static void main(String[] args) throws MalformedPatternException {

		String variable_regexp = "^\\$(\\w+)";

		String function_regexp = "^\\$\\{(\\w+)\\(([\\w,]*)\\)\\}$";

		String function_regexp_fun = "^\\$\\{(\\w+)\\((.*)\\)\\}$";

		String var = "$tasdsads";
		String funvar="${func(a,522)}";
		String funvarfun="${func($func(6,10), 5a,10b#)}";

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
	      // 创建 Pattern 对象
	      Pattern r = Pattern.compile(function_regexp_fun);
	      // 现在创建 matcher 对象
	      Matcher m = r.matcher(funvarfun);
	      if (m.find( )) {
	    	  System.out.println(m.groupCount());
	          System.out.println("Found value: " + m.group(0) );
	          System.out.println("Found value: " + m.group(1) );
	          System.out.println("Found value: " + m.group(2) );
	       } else {
	          System.out.println("NO MATCH");
	       }

	}

}
