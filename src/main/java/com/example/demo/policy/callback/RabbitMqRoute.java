package com.example.demo.policy.callback;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HanXiong on 2017/8/21.
 * <p>
 * rabbitmq://user:password@127.0.0.1:1234,127.0.0.2:1234/virtualhost?exchange=x&routingkey=x
 */
public class RabbitMqRoute implements Route {

	private String uri;

	private String user;

	private String password;

	private List<String> addressList;

	private String virtualHost;

	private String exchange;

	private String routingKey;

	private Map<String, String> extInfo;

	private AmqpTemplate amqpTemplate;

	private CachingConnectionFactory cf;

	public RabbitMqRoute(String uri) {
		this.uri = uri;
		parseUri(uri);
	}

	private void parseUri(String uri) {

		int schemaEnd = uri.indexOf("://", 0);
		if (schemaEnd <= 0) {
			throw new IllegalArgumentException("parse as rabbitmq uri fail, uri:" + uri);
		}

		String schema = uri.substring(0, schemaEnd);
		if (!schema.equals(getSchema())) {
			throw new IllegalArgumentException("parse as rabbitmq uri fail, uri:" + uri);
		}

		int credentialStart = schemaEnd + "://".length();
		int credentialEnd = uri.indexOf("@", credentialStart);
		parseCredential(credentialStart, credentialEnd);

		int addressStart = credentialEnd + "@".length();
		int addressEnd = uri.indexOf("/", addressStart);
		parseAddressList(addressStart, addressEnd);

		int virtualHostStart = addressEnd + "/".length();
		int virtualHostEnd = uri.indexOf("?");
		virtualHostEnd = virtualHostEnd == -1 ? uri.length() : virtualHostEnd;
		parseVirtualHost(virtualHostStart, virtualHostEnd);

		int extInfoStart = virtualHostEnd + "?".length();
		int extInfoEnd = uri.length();
		parseExtInfo(extInfoStart, extInfoEnd);

	}

	private void parseVirtualHost(int f, int t) {
		String v = uri.substring(f, t);
		try {
			this.virtualHost = URLDecoder.decode(v, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
	}

	private void parseCredential(int f, int t) {
		String c = uri.substring(f, t);
		String[] sp = c.split(":");

		this.user = sp[0].trim();
		this.password = sp[1].trim();
	}

	private void parseAddressList(int f, int t) {
		String a = uri.substring(f, t);
		String[] sp = a.split(",");
		this.addressList = Lists.newArrayList(sp);
	}

	private void parseExtInfo(int f, int t) {
		this.extInfo = new HashMap<>();

		String e = uri.substring(f, t);
		String[] kv = e.split("&");
		for (String s : kv) {

			String[] ss = s.split("=");
			String k = ss[0];
			String v = ss[1];

			 this.extInfo.put(k, v);
		}

		this.exchange = this.extInfo.get("exchange");
		this.routingKey = this.extInfo.get("routingKey");
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public String getSchema() {
		return "rabbitmq";
	}

	@Override
	public String getUser() {
		return user;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public List<String> getAddressList() {
		return addressList;
	}

	@Override
	public void initConnection() {

		cf = new CachingConnectionFactory();
		cf.setAddresses(StringUtils.join(addressList, ",") );
		cf.setUsername(getUser());
		cf.setPassword(getPassword());
		cf.setVirtualHost(virtualHost);
		cf.setPublisherConfirms(true); // 必须要设置

		amqpTemplate = new RabbitTemplate(cf);
	}

	public AmqpTemplate getAmqpTemplate() {
		return amqpTemplate;
	}

	public String getExchange() {
		return exchange;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	@Override
	public void close() {
		if (cf != null) {
//			cf.stop(); //版本不一致 低版本里面有stop方法
		}
	}
}
