package com.example.demo.util.socket;

import java.io.*;
import java.net.Socket;

/**
 * Created by HMa on 2018/8/9.
 */
public class SocketClient {

	public static void main(String[] args) {
		try {
			Socket socket =new Socket("localhost",10086);
			OutputStream outputStream=socket.getOutputStream();//字节输出流
			PrintWriter pw=new PrintWriter(outputStream);
			pw.write("用户名：admin，密码是：123");
			pw.flush();;
			socket.shutdownOutput();
			InputStream is=socket.getInputStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
			String str=null;
			while((str=br.readLine())!=null){
				System.out.println("我是客户端，服务器响应内容："+str);
			}

			//关闭资源
			br.close();
			is.close();
			pw.close();
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
