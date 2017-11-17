package com.fengdai.qa.meta;

import java.util.ArrayList;

import org.json.JSONObject;

public class app {

    /**
     * @param args
*/
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        /*简单实现一个树的结构，后续完善解析xml             */
        /*写得满烂的，后续查阅一些其他代码                2012-3-12    */
        //测试
        /*
         * string
         *         hello
         *             sinny
         *             fredric
         *         world
         *           Hi
         *           York
         * */

        tree<String> tree = new tree();
        ArrayList<JSONObject> treeJsonObject = new ArrayList<JSONObject>();
        tree.addNode(null, "funa");
        tree.addNode(tree.getNode("funa"), "funb");
        tree.addNode(tree.getNode("funa"), "z");
        tree.addNode(tree.getNode("funa"), "m");
        tree.addNode(tree.getNode("funb"), "x");
        tree.addNode(tree.getNode("funb"), "y");
//        tree.showNode(tree.root,0);
        tree.getTreeString(tree.root, 1, treeJsonObject);
        System.out.println(treeJsonObject.toString());
        tree.getNode("funb").t="mmmm";

        tree.getTreeString(tree.root, 1, treeJsonObject);
        System.out.println(treeJsonObject.toString());
        System.out.println("end of the test");
    }

}