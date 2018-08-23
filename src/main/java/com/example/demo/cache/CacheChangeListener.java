package com.example.demo.cache;

/**
 * Created by HanXiong on 2017/8/21.
 */
public interface CacheChangeListener<T extends CacheData> {

	void onChange(String key, T newValue, T oldValue);
}
