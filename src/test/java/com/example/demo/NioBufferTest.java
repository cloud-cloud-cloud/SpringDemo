package com.example.demo;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by HMa on 2018/7/17.
 */
public class NioBufferTest  {

	@Test
	public void testBuffer() {
		ByteBuffer buffer=ByteBuffer.allocate(10);//底层为10的byte数组
		System.out.println(buffer.capacity());
		System.out.println(buffer.limit());
		System.out.println(buffer.mark());
		System.out.println(buffer.position());
		System.out.println("bbg".getBytes());
		System.out.println("22222222222");
		System.out.println(buffer.capacity());
		System.out.println(buffer.limit());
		System.out.println(buffer.position());//每次put都会将值移动一次
		System.out.println("..........flip............");
		buffer.flip();//将存入数据模式变为取出数据模式，已经存入的数据position又变为0，从头开始继续读
		System.out.println(buffer.capacity());
		System.out.println(buffer.limit());
		System.out.println(buffer.position());

		System.out.println("..........get()...........");
		System.out.println((char)buffer.get());
		System.out.println((char)buffer.get());


		System.out.println(buffer.capacity());
		System.out.println(buffer.limit());
		System.out.println(buffer.position());

		buffer.rewind();//重置position
		buffer.clear();//清空  回到用户最初的状态  10，10，0

		System.out.println((char)buffer.get());


	}

	/**
	 * 非直接缓冲区
	 * @throws Exception
	 */
	@Test
	public void TestChannel() throws  Exception{
		FileInputStream fileInputStream=new FileInputStream("D:/xxx.txt");
		FileOutputStream fileOutputStream=new FileOutputStream("D:/a.txt");

		ByteBuffer buffer=ByteBuffer.allocate(1024);
		FileChannel inChannel=fileInputStream.getChannel();
		FileChannel outChannel=fileOutputStream.getChannel();

		while(inChannel.read(buffer)!=-1){

			buffer.flip();//去换到读数据的模式，如果不切换，缓冲区会满
			outChannel.write(buffer);
			buffer.clear();//清空
		}
		fileInputStream.close();
		fileOutputStream.close();
		inChannel.close();
		outChannel.close();
	}

	/**
	 * 直接缓冲区
	 */

	@Test
	public void testChannel2() throws Exception{
		//1.创建Channel
		FileChannel inChannel=FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		FileChannel outChannel=FileChannel.open(Paths.get("3.jpg"),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		FileChannel inChannel1=FileChannel.open(Paths.get("D:/xxx.txt"), StandardOpenOption.READ);
		FileChannel outChannel1=FileChannel.open(Paths.get("D:/a.txt"),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		//2.创建得到直接缓冲区
		MappedByteBuffer inMappedBuffer=inChannel.map(FileChannel.MapMode.READ_ONLY,0,inChannel.size());
		MappedByteBuffer outMapedBuffer=outChannel.map(FileChannel.MapMode.READ_WRITE,0,inChannel.size());
        byte[] bytes=new byte[inMappedBuffer.limit()];
		//数据的读写
		inMappedBuffer.get(bytes);
		outMapedBuffer.put(bytes);
		//关闭资源
		inChannel.close();
		outChannel.close();;
	}


}
