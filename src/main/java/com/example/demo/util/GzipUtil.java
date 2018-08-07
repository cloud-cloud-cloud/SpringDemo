package com.example.demo.util;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by HMa on 2018/7/25.
 */
public class GzipUtil {

	public static String compressBase64(String str)throws Exception{
		if(StringUtils.isEmpty(str)){
			return "";
		}
		byte[] toArray=compress(str);
		return new BASE64Encoder().encode(toArray);
	}

	/**
	 * 压缩
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(String str) throws  IOException{
		if(StringUtils.isEmpty(str)){
			return new byte[0];
		}
		byte[] bytes;
		GZIPOutputStream gzipOutputStream=null;
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		try {
			gzipOutputStream=new GZIPOutputStream(byteArrayOutputStream);
			gzipOutputStream.write(str.getBytes("UTF-8"));
			gzipOutputStream.flush();

		}finally {
			gzipOutputStream.close();
		}
		bytes=byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();;
		return  bytes;
	}
	public static byte[] uncompress(byte[] str) throws IOException {
		java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(str);
		GZIPInputStream gunzip = new GZIPInputStream(in);
		try {
			byte[] buffer = new byte[256];
			int n;
			while ((n = gunzip.read(buffer)) >= 0) {
				out.write(buffer, 0, n);
			}
		} finally {
			gunzip.close();
		}
		in.close();
		out.close();

		return out.toByteArray();
	}

	/**
	 * 解压缩
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String uncompressWithDecodeBase64(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return "";
		}

		BASE64Decoder tBase64Decoder = new BASE64Decoder();
		byte[] t = tBase64Decoder.decodeBuffer(str);

		byte[] result = uncompress(t);

		return new String(result, "UTF-8");
	}

	public static String uncompress(InputStream inputStream) throws IOException {
		InputStream gzipInputStream = new GZIPInputStream(inputStream,1024*1024);
		Reader reader = new InputStreamReader(gzipInputStream, "UTF-8");
		char[] buffer = new char[10240];
		StringWriter writer = new StringWriter();
		for(int length = 0; (length = reader.read(buffer)) > 0;){
			writer.write(buffer, 0, length);
		}
		writer.close();
		reader.close();
		gzipInputStream.close();
		inputStream.close();
		return writer.toString();
	}
}
