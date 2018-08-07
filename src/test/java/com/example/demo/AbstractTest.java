package com.example.demo;

/**
 * Created by HMa on 2018/8/2.
 * 抽象类不能实例化
 */
public abstract class AbstractTest {
	private int age;


	public void hello(){
		System.out.println("hello");
	}
	public abstract void word();
}
