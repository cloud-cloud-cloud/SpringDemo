package com.example.demo.policy.callback;

import java.util.List;

/**
 * Created by HanXiong on 2017/8/21.
 */
public interface Route {

	String getUri();

	String getSchema();

	String getUser();

	String getPassword();

	List<String> getAddressList();

	void initConnection();

	void close();
}
