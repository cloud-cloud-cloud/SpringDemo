package com.example.demo.thread;

import java.util.concurrent.Executors;

/**
 * Created by HMa on 2018/7/20.
 */
public class ThreadTest {
	public volatile int inc=0;
	public static void main(String[] args) {
		new Thread() {

		}.start();


	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			for (int i = 0; i < 50; i++) {
				System.out.println(Thread.currentThread().getName() + "_____" + i);
			}
		}
	};
		new Thread(runnable).start();
}



	public void executorTest()
	{
		Executors.newFixedThreadPool(10);//定长线程池
		Executors.newScheduledThreadPool(10);//可定时执行，延迟执行的任务池
		Executors.newSingleThreadScheduledExecutor();//该线程池是定长的，不用自己定义长度；
		Executors.newCachedThreadPool();//该线程大小基本没有限制，因为限制值比较大，该线程比较适合执行很多短期异步的任务的小程序，或者负载较轻的服务器。
	}
}
