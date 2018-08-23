package com.example.demo.components.mail;

import com.alibaba.fastjson.JSON;
import com.example.demo.cache.EmailConfigCache;
import com.example.demo.entity.EmailCfg;
import com.example.demo.util.Lifecycle;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.*;

@Component
public class EmailCenter implements Lifecycle, InitializingBean, DisposableBean {

	protected Logger logger = LoggerFactory.getLogger(EmailCenter.class);

	private int sendThreadNum = 2;

	private int retryTimesIfFail = 2;

	private int queueCapacity = 10000;

	private BlockingQueue<SendEmailRequest> mailQueue;

	private ExecutorService es;

	private volatile boolean stop = false;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.from}")
	private String mailFrom;

	@Autowired
	private EmailConfigCache emailConfigCache;

	private static final String EMAIL_REG_SPLIT = ",";

	@Override
	public void start() {
		mailQueue = new LinkedBlockingDeque<SendEmailRequest>(queueCapacity);
		es = Executors.newFixedThreadPool(sendThreadNum, new BasicThreadFactory.Builder().namingPattern("email-center-%d").build());

		logger.info("EmailCenter startup, sendThreadNum:{}, queueCapacity:{}", sendThreadNum, queueCapacity);

		for (int i = 0; i < sendThreadNum; i++) {
			es.submit(new EmailSendingRunner());
		}
	}

	@Override
	public void stop() {
		stop = true;
		es.shutdownNow();
	}


	public void send(SendEmailRequest em) {
		if (em == null) {
			return;
		}

		if (!mailQueue.offer(em)) {
			logger.warn("email sending pool is full, current mailQueue size: {}, email discard, content: ", mailQueue.size(), em.toString());
		}
	}

	private void doSend(SendEmailRequest ew) throws MessagingException, IOException, TemplateException {

		EmailCfg emailCfg = emailConfigCache.getCacheData(ew.getEmailType());

		if (emailCfg == null) {
			logger.warn("cannot find email type: {}, attr: {}", ew.getEmailType(), JSON.toJSONString(ew.getEmailAttribute()));
			return;
		}

		StringWriter contentWriter = new StringWriter();
		emailCfg.getFmTemplate().process(ew.getEmailAttribute(), contentWriter);
		doSend(contentWriter.toString(), emailCfg);
	}

	private void doSend(String content, EmailCfg cfg) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

		messageHelper.setFrom(mailFrom);
		messageHelper.setSubject(cfg.getSubject());
		messageHelper.setText(content, true);
		messageHelper.setTo(cfg.getReceiverList().split(EMAIL_REG_SPLIT));

		if (!StringUtils.isEmpty(cfg.getCcList())) {
			messageHelper.setCc(cfg.getCcList().split(EMAIL_REG_SPLIT));
		}

		mailSender.send(mimeMessage);
	}


	private class EmailSendingRunner implements Runnable {

		@Override
		public void run() {

			while (!stop && !Thread.currentThread().isInterrupted()) {
				SendEmailRequest ew = null;
				try {
					ew = mailQueue.poll(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					logger.warn("Email Sending Runner interrupted");
					Thread.currentThread().interrupt();
				}

				if (ew == null) {
					continue;
				}

				Exception ex = null;
				int tryTimes = 0;
				for (int i = 0; i < retryTimesIfFail + 1; i++) {
					try {
						tryTimes++;
						doSend(ew);
						break;
					} catch (Exception e) {
						ex = e;
						if (!(e instanceof MessagingException)) {
							break;
						}
					}
				}

				if (ex != null) {
					logger.info("Email center send email exception, mail content: {}, retryTimes: {}", ew, tryTimes, ex);
				}
			}

			logger.warn("Email Sending Runner quit...");
		}
	}


	@Override
	public void destroy() throws Exception {
		stop();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		start();
	}
}
