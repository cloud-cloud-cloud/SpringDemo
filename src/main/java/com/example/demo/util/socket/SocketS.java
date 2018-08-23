package com.example.demo.util.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by HMa on 2018/8/9.
 * 基于tcp协议的socket通信，实现客户端登录，
 */
public class SocketS {




	public static void main(String[] args) {
		try {
			ServerSocket serverSocket=new ServerSocket(10086);
			Socket socket=serverSocket.accept();//监听客户端
			InputStream is=socket.getInputStream();//读取客户端信息；
			BufferedReader br=new BufferedReader(new InputStreamReader(is));//包装流
			String info=null;
			while((info=br.readLine())!=null){
				System.out.println("我是服务端，接收的客户端内容："+info);
			}
			socket.shutdownInput();//关闭输入流
			OutputStream outputStream=socket.getOutputStream();//输出流，响应客户端请求
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outputStream));
			bw.write("欢迎你！");
			bw.flush();

			//关闭资源
			bw.close();
			outputStream.close();
			br.close();
			is.close();
			socket.close();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
