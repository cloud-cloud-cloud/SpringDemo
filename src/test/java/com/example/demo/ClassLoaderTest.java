package com.example.demo;

/**
 * Created by HMa on 2018/7/13.
 */
public class ClassLoaderTest {
	public static void main(String[] args) {
		String[] strs=System.getProperty("sun.boot.class.path").split(";");//bootstrap启动类加载的主要是jdk/jre/lib下面的 运行环境的重要jar包已经class
		String[] extrensions=System.getProperty("java.ext.dirs").split(";");//加载jdk/jar/lib/ext里面的扩展jar
		String[] apps=System.getProperty("java.class.path").split(";");//加载应用的class  以及应用jar
		for(String str:strs){
			System.out.println("bootstrap classLoad:"+str);
		}
        for(String str:extrensions){
			System.out.println("Extension classLoader:"+str);
		}
		for(String str:apps){
			System.out.println("app classLoader:"+str);
		}
	}
}
