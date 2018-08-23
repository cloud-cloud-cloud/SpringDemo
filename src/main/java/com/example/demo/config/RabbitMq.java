package com.example.demo.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * Created by HMa on 2018/8/9.
 */
public class RabbitMq {
	@Autowired
	private RabbitMqNoticeConfig noticeProperties;
	@Autowired
	private RabbitMqUserratingConfig userratingProperties;

	@Bean(value = "xdtConnectionFactory")
	public ConnectionFactory conectionFactory(){
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(userratingProperties.getAddresses());
		connectionFactory.setUsername(userratingProperties.getUsername());
		connectionFactory.setPassword(userratingProperties.getPassword());
		connectionFactory.setVirtualHost(userratingProperties.getVirtualHost());
		connectionFactory.setPublisherConfirms(userratingProperties.isPublisherConfirms()); // 必须要设置

		return connectionFactory;

	}

	@Bean(value = "noticeTemplate")
	public RabbitTemplate noticeTemplate() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(noticeProperties.getAddresses());
		connectionFactory.setUsername(noticeProperties.getUsername());
		connectionFactory.setPassword(noticeProperties.getPassword());
		connectionFactory.setVirtualHost(noticeProperties.getVirtualHost());
		connectionFactory.setPublisherConfirms(noticeProperties.isPublisherConfirms()); // 必须要设置

		return new RabbitTemplate(connectionFactory);
	}

}
