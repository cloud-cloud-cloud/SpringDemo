package com.example.demo.thread;

/**
 * Created by HMa on 2018/7/17.
 */
public class Counter {
	public volatile static  int counter=0;

	public static  void inc(){
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		counter++;

	}

	public static void main(String[] args) {
		for(int i=0;i<1000;i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					inc();
				}
			}).start();
		}
		System.out.println("运行结果counter.count="+counter);
	}
}
