package com.example.demo.thread;

import java.util.concurrent.Exchanger;

/**
 * Created by HMa on 2018/7/17.
 */

	public class SendAndReceiver{
		private final Exchanger<StringBuilder> exchanger = new Exchanger<StringBuilder>();
		private class Sender implements Runnable{
			public void run(){
				try{
					StringBuilder content = new StringBuilder("Hello");
					content = exchanger.exchange(content);
				}catch(InterruptedException e){
					Thread.currentThread().interrupt();
				}
			}
		}
		private class Receiver implements Runnable{
			public void run(){
				try{
					StringBuilder content = new StringBuilder("World");
					content = exchanger.exchange(content);
				}catch(InterruptedException e){
					Thread.currentThread().interrupt();
				}
			}
		}
		public void exchange(){
			new Thread(new Sender()).start();
			new Thread(new Receiver()).start();
		}
	}


