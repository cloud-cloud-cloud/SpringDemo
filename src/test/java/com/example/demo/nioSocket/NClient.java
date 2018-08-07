package com.example.demo.nioSocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Created by HMa on 2018/7/30.
 */
public class NClient {
	private Selector selector=null;
	private Charset charset=Charset.forName("UTF-8");
	private SocketChannel sc=null;
	public void init() throws IOException{
		selector=Selector.open();
		sc=SocketChannel.open(new InetSocketAddress("127.0.0.1",30000));
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
		new ClientThread().start();
		Scanner scanner=new Scanner(System.in);
		while(scanner.hasNextLine()){
			String line=scanner.nextLine();
			sc.write(charset.encode(line));
		}
	}


	private class ClientThread extends  Thread{
		public void run(){
			try{
			while(selector.select()>0){
				//遍历每个有可用IO操作Channel对应的SelectionKey
				for(SelectionKey sk:selector.selectedKeys()){
					//删除正在处理的SelectionKey
					selector.selectedKeys().remove(sk);
					//如果该SelectionKey对应的Channel中有可读的数据
					if(sk.isReadable()){
						//使用NIO读取channel中的数据
						SocketChannel sc=(SocketChannel) sk.channel();
						ByteBuffer buff=ByteBuffer.allocate(1024);
						String content="";
						while(sc.read(buff)>0){
							//sc.read(buff);
							buff.flip();
							content+=charset.decode(buff);
						}
						//打印输出读取的内容
						System.out.println("聊天信息"+content);
						//为下一次读取做准备
						sk.interestOps(SelectionKey.OP_READ);
					}
				}
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}

	}
		}

}
