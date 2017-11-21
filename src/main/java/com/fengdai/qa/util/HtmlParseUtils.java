package com.fengdai.qa.util;
import java.util.HashMap;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.tags.InputTag;
import org.htmlparser.util.NodeList;





/**
 * JAVA中使用Htmlparse解析HTML文档，使用htmlparse遍历出HTML文档的所有超链接（<a>标记）。
 *
 * @author YYmmiinngg
 */
public class HtmlParseUtils {
    public static void getbrand() {
        try {
			/* 首先我们先使用HttpRequester类和HttpRespons类获得一个HTTP请求中的数据（HTML文档）。 可以从(http://download.csdn.net/source/321516)中下载htmlloader，该库中有上述类；或从我的《JAVA发送HTTP请求，返回HTTP响应内容，实例及应用》一文中摘取上述两JAVA类的代码。htmlparse可以从(http://download.csdn.net/source/321507)中下载
			*/
            Map<String, String> map = new HashMap<String, String>();
            String result = FileUtil.readFileByLines("2.html");

            Parser parser = Parser.createParser(result,"utf-8");

            try {
                // 通过过滤器过滤出<input>标签
                NodeList nodeList = parser
                        .extractAllNodesThatMatch(new NodeFilter() {
                            //实现该方法,用以过滤标签
                            public boolean accept(Node node) {
                                if (node instanceof InputTag)//<A>标记
                                    return true;
                                return false;
                            }
                        });
                // 打印
                for (int i = 0; i < nodeList.size(); i++) {
                    InputTag n = (InputTag) nodeList.elementAt(i);
                    if("brandId".equals(n.getAttribute("id")) && "display:none".equals(n.getAttribute("style"))) {
                    	System.out.print(n.getText() + " ==>> ");
                    	System.out.println(n.getAttribute("value"));
                    	break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	getbrand();
	}
}