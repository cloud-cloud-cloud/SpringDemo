package com.example.demo.policy.callback;

import com.example.demo.cache.CacheChangeListener;
import com.example.demo.cache.CallbackConfigCache;
import com.example.demo.entity.CallbackCfg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by HanXiong on 2017/8/21.
 * <p>
 * 可能存在多种回调方式，多个回调地址
 * 每一个回调配置都是一个Route，用来应对回调配置中潜在的差异性
 */
@Component
public class CallbackRouteManager implements ApplicationListener<ApplicationReadyEvent>, DisposableBean {

	private static final Logger logger = LoggerFactory.getLogger(CallbackRouteManager.class);

	@Autowired
	private CallbackConfigCache callbackConfigCache;

	private RouteFactory routeFactory = new RouteFactory();

	private Map<String, Route> routes = new ConcurrentHashMap<>();

	@Override
	public void destroy() throws Exception {
		for (Route r : routes.values()) {
			r.close();
		}
	}

	public Route getRoute(String policyType) {
		return routes.get(policyType);
	}


	private void registerRoute(String k, Route r) {
		if (r != null) {
			r.initConnection();
			Route old = routes.put(k, r);
			if (old != null) {
				old.close();
			}
		}
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		Map<String, CallbackCfg> all = callbackConfigCache.getAll();
		for (CallbackCfg cfg : all.values()) {
			Route route = routeFactory.create(cfg.getInvokeUrl());

			logger.info("init route: {}, {}", cfg.getDataKey(), route.getUri());
			registerRoute(cfg.getPolicyType(), route);
		}

		callbackConfigCache.register(new CacheChangeListener<CallbackCfg>() {
			@Override
			public void onChange(String key, CallbackCfg newValue, CallbackCfg oldValue) {
				String policyType = newValue.getPolicyType();
				String newUri = newValue.getInvokeUrl();

				Route route = routeFactory.create(newUri);
				registerRoute(policyType, route);
			}
		});
	}
}
