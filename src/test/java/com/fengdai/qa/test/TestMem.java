package com.fengdai.qa.test;

public class TestMem {

	public static void main(String[] args) {
		    new TestMem().doTest();
		}

		public void doTest(){
		    Integer M = new Integer(1024 * 1024 * 1);  //单位, 兆(M)
		    byte[] bytes = new byte[1 * M]; //申请 1M 大小的内存空间
		    bytes = null;  //断开引用链
		    System.gc();   //通知 GC 收集垃圾
		    System.out.println();
		    bytes = new byte[1 * M];  //重新申请 1M 大小的内存空间
		    bytes = new byte[1 * M];  //再次申请 1M 大小的内存空间
		    System.gc();
		    System.out.println();
		}

}
