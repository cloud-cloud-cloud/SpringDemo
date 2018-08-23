package com.example.demo.components.mail;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HanXiong on 2017/7/20.
 */
public class SendEmailRequest {

	private String emailType;

	private Map<String, Object> emailAttribute;

	public SendEmailRequest(String emailType, Map<String, Object> emailAttribute) {
		this.emailType = emailType;
		this.emailAttribute = emailAttribute;
	}

	public SendEmailRequest(String emailType) {
		this(emailType, new HashMap<String, Object>());
	}

	public SendEmailRequest addAttribute(String k, Object v) {
		emailAttribute.put(k, v);
		return this;
	}

	public String getEmailType() {
		return emailType;
	}

	public Map<String, Object> getEmailAttribute() {
		return emailAttribute;
	}
}
