package com.example.demo.util.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by HMa on 2018/8/9.
 * 创建多线程socketServer与多个客户端进行通讯
 */
public class SocketThread {
	Socket socket=null;

	public SocketThread(Socket socket){
		this.socket=socket;
	}
	public void  run(){
		try {
			ServerSocket serverSocket=new ServerSocket(10087);
			Socket socket=null;
			int count=0 ;//统计客户端数量
			while(true){
				socket=serverSocket.accept();
				SocketThread serverThread=new SocketThread(socket);
//				serverThread.start();//todo  待补充
				count++;
				System.out.println("客户端连接的数量"+count);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
