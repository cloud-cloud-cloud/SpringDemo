package com.example.demo.entrance;

import com.example.demo.components.mail.EmailCenter;
import com.example.demo.components.mail.SendEmailRequest;
import com.example.demo.util.IdGenerator;
import com.example.demo.util.IntervalProtectionRunner;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.function.Function;

/**
 * Created by HanXiong on 2017/8/21.
 */
@Configuration
public class RabbitmqContainers {

	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitmqContainers.class);



	@Autowired
	private IdGenerator idGenerator;

	@Autowired
	private EmailCenter emailCenter;

	@Autowired
	@Qualifier(value = "xdtConnectionFactory")
	private ConnectionFactory xdtConnectionFactory;

	@Bean(name = "userRatingMessageContainer")
	public SimpleMessageListenerContainer userRatingMessageContainer() {

		Queue userRatingQueue = new Queue("ndes.process.start.userrating", true);

		/**
		 *  首先创建 RabbitMQ 管理对象，auto declare由这个对象完成
		 * 	AmqpAdmin admin = new RabbitAdmin(xdtConnectionFactory);
		 *
		 *  控制台中相关依赖配置
		 *
		 * 	TopicExchange xdtExchange = new TopicExchange("xdt");
		 *	admin.declareExchange(xdtExchange);
		 *
		 *	TopicExchange dpExchange = new TopicExchange("dp");
		 *	admin.declareExchange(dpExchange);
		 *
		 * 	Queue userRatingQueue = new Queue("ndes.process.start.userrating", true);
		 *	admin.declareQueue(userRatingQueue);
		 *
		 *	admin.declareBinding(BindingBuilder.bind(userRatingQueue).to(xdtExchange).with("loan.#.repay.book.notice"));
		 *	admin.declareBinding(BindingBuilder.bind(userRatingQueue).to(dpExchange).with("dp.loan.pdl.contract.update.grace.finished.notice"));
		 *
		 */
		final Set<String> awareOfRoutingKeys = ImmutableSet.of(
				"loan.adv.repay.book.notice",        //Q易借&暖薪贷大数据平台-提前还款冲账成功通知
				"loan.overdue.repay.book.notice",    //Q易借&暖薪贷大数据平台-逾期还款冲账成功通知接口
				"loan.repay.book.notice",            //Q易借&暖薪贷大数据平台-正常还款冲账成功通知
				"dp.loan.pdl.contract.update.grace.finished.notice"
		);


		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(xdtConnectionFactory);
		container.setQueues(userRatingQueue);
		container.setExposeListenerChannel(true);
		container.setMaxConcurrentConsumers(1);
		container.setConcurrentConsumers(1);
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
		container.setMessageListener(new ChannelAwareMessageListener() {

			// 出现失败则发送报警邮件。为避免一段时间内出现大量失败而产生大量邮件，做一个时间保护，10分钟内只发送一次邮件
			private final IntervalProtectionRunner<Void, Void> protectedEmailSender = new IntervalProtectionRunner<>(1000L * 60 * 10, new Function<Void, Void>() {

				@Override
				public Void apply(Void t) {
					emailCenter.send(new SendEmailRequest("processWarningReport").addAttribute("content", "An abnormal data is generated when N-des consuming MQ data!"));
					return null;
				}
			});

			public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
				final String recvRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
				final String messageContent = new String(message.getBody(), "utf-8");
				if (!awareOfRoutingKeys.contains(recvRoutingKey)) {

					LOGGER.warn("unexpected routing key received: {}, content: {}", recvRoutingKey, messageContent);
					channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
					return;
				}
				if (StringUtils.isBlank(messageContent)) {
					LOGGER.warn("routing key {} received as null data, content: {}", recvRoutingKey, messageContent);
					channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
					return;
				}
				LOGGER.info("userRatingMessageContainer receive message from xdt, routingKey:{}, message : {}", recvRoutingKey, messageContent);


			}
		});
		return container;
	}



}
