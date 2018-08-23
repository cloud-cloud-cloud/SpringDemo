package com.example.demo.policy.callback;

import com.example.demo.util.SpringContextHelper;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.List;

/**
 * Created by HanXiong on 2017/8/22.
 */
public class HttpRoute implements Route {

	private CloseableHttpClient httpClient;

	private String url;

	private int connectionTimeout = 30 * 1000;

	private int socketTimeout = 2 * 60 * 1000;

	public HttpRoute(String uri) {
		this.url = uri;
	}

	@Override
	public String getUri() {
		return url;
	}

	@Override
	public String getSchema() {
		return "http";
	}

	@Override
	public String getUser() {
		return null;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public List<String> getAddressList() {
		return null;
	}

	@Override
	public void initConnection() {
		httpClient = SpringContextHelper.getBean("ruleResultPushHttpClient");
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public String getUrl() {
		return url;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	@Override
	public void close() {
		httpClient = null;
	}
}
