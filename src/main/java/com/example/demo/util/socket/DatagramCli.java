package com.example.demo.util.socket;

import java.net.*;

/**
 * Created by HMa on 2018/8/9.
 */
public class DatagramCli {

	public static void main(String[] args) {
		try {
			InetAddress address=InetAddress.getByName("localhost");
			int port=10090;
			byte[] data ="用户名：admin;密码：123".getBytes();
			//2、创建数据报，包含发送的数据信息
			 DatagramPacket packet = new DatagramPacket(data,data.length,address,port);
			 //3、创建DatagramSocket对象
			DatagramSocket socket = null;

				socket = new DatagramSocket();
				//4、向服务器发送数据
				socket.send(packet);

				//接受服务器端响应数据
				//======================================
				//1、创建数据报，用于接受服务器端响应数据
				byte[] data2 = new byte[1024];
				DatagramPacket packet2 = new DatagramPacket(data2,data2.length);
				//2、接受服务器响应的数据
				socket.receive(packet2);
				String raply = new String(data2,0,packet2.getLength());
				System.out.println("我是客户端，服务器说："+raply);

				socket.close();	 //4、关闭资源
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
