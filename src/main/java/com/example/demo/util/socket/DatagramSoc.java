package com.example.demo.util.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by HMa on 2018/8/9.
 * 服务器端  基于UDP的用户登录
 */
public class DatagramSoc {
	public static void main(String[] args) {
		try {
			DatagramSocket ds=new DatagramSocket(10090);//创建服务端datagramsocket，指定端口
			byte[] bytes=new byte[1024];//用于接收客户端发送的数据
			DatagramPacket  dp=new DatagramPacket(bytes,bytes.length);//表示数据报包，接收客户端发送的数据
			try {
				ds.receive(dp);//接收数据报之前会一致阻塞
				String  info=new String(bytes,0,bytes.length);
				System.out.println("我是服务端，客户端内容："+info);

				//向客户端响应数据，
				InetAddress address=dp.getAddress();//定义客户端的地址，端口，数据
				int port=dp.getPort();
				byte[] bytes1="欢迎你".getBytes();
				DatagramPacket packet=new DatagramPacket(bytes1,bytes1.length,address,port);//创建数据报，包含响应的数据信息
				ds.send(packet);//响应客户端

				ds.close();//关闭资源


			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
