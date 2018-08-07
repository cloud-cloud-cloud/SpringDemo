package com.example.demo;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.MessageDigest;

/**
 * Created by HMa on 2018/7/23.
 */
public class EncryptionDecryptionTest {
	/**
	 * base64加密
	 * @param password
	 * @return
	 */
	public static String encryptBase64(String password){
		byte[] bytes=password.getBytes();
		return (new BASE64Encoder()).encode(bytes);
	}

	/**
	 * 解密base64
	 * @param key
	 * @return
	 */
	public static String decryptBase64(String key){
		try {
			byte[] bytes=(new BASE64Decoder()).decodeBuffer(key);
			return new String(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return  null;
	}

	/**
	 * md5加密算法
	 * @param passw
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryMD5(String passw) throws Exception {
		byte[] bytes=passw.getBytes();
		MessageDigest messageDigest=MessageDigest.getInstance("MD5");
		messageDigest.update(bytes);
		return messageDigest.digest();
	}

	public static byte[] encrySHA(String passw) throws Exception{
		byte[] bytes=passw.getBytes();

		MessageDigest messageDigest=MessageDigest.getInstance("SHA");
		messageDigest.update(bytes);
		return messageDigest.digest();


	}
}
