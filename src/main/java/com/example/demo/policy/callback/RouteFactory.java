package com.example.demo.policy.callback;

/**
 * Created by HanXiong on 2017/8/22.
 */
public class RouteFactory {

	public Route create(String uri) {
		String schema = extractSchema(uri);

		if (schema.equals("rabbitmq")) {
			return new RabbitMqRoute(uri);
		} else if (schema.equals("http")) {
			return new HttpRoute(uri);
		}

		return null;
	}

	private String extractSchema(String uri) {
		int p = uri.indexOf("://");

		if (p == -1) {
			return null;
		}
		return  uri.substring(0, p);
	}
}
