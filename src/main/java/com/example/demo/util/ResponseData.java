/**
 * @Title:   [ResponseData.java]
 * @Package: [com.qf.uca.util]
 * @author:  [ChangcaiCao] 
 * @CreateDate: [2016年7月14日 上午11:09:56]   
 * @UpdateUser: [ChangcaiCao]   
 * @UpdateDate: [2016年7月14日 上午11:09:56]   
 * @UpdateRemark: [说明本次修改内容]
 * @Description:  [TODO(用一句话描述该文件做什么)]
 * @version: [V1.0]
 */
package com.example.demo.util;

/**
 * @ClassName: ResponseData
 * @author:  [ChangcaiCao] 
 * @CreateDate: [2016年7月14日 上午11:09:56]   
 * @UpdateUser: [ChangcaiCao]   
 * @UpdateDate: [2016年7月14日 上午11:09:56]   
 * @UpdateRemark: [说明本次修改内容]
 * @Description:  [TODO(用一句话描述该文件做什么)]
 * @version: [V1.0]
 */
public class ResponseData<T> {

	/*
	 * 响应编码
	 */
	private int responseCode;
	
	/*
	 * 响应消息
	 */
	private String responseMessage;
	
	/*
	 * 返回的数据
	 */
	private T responseBody;

	public ResponseData() {
	}

	public ResponseData(int responseCode, String responseMessage, T responseBody) {
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.responseBody = responseBody;
	}

	

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public T getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(T responseBody) {
		this.responseBody = responseBody;
	}

	public static ResponseData success() {
		return new ResponseData(ResponseCode.SUCCESS, null, null);
	}

	public static ResponseData success(Object responseBody) {
		return new ResponseData(ResponseCode.SUCCESS, null, responseBody);
	}
}
