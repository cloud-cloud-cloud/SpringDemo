package com.example.demo.cache;

import com.example.demo.dao.EmailCfgMapper;
import com.example.demo.entity.EmailCfg;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;


@Component
public class EmailConfigCache extends IncrementalCache<EmailCfg> implements InitializingBean, DisposableBean {

	private static final Logger logger = LoggerFactory.getLogger(EmailConfigCache.class);

	@Autowired
	private EmailCfgMapper emailCfgMapper;

	@Autowired
	private Configuration configuration;

	@Value("30")
	private int refreshRate;

	@Override
	protected EmailCfg buildCacheData(EmailCfg newCfg) {
		try {
			newCfg.setFmTemplate(new Template(newCfg.getEmailType(), new StringReader(newCfg.getTemplate()), configuration));
			return newCfg;
		} catch (IOException e) {
			logger.error("", e);
			return null;
		}
	}

	@Override
	protected List<EmailCfg> doLoadData(long lastUpdateTime) {
		EmailCfg cfgParam = new EmailCfg();
		cfgParam.setUpdateTime(new Date(lastUpdateTime));
		return emailCfgMapper.selectByGreaterUpdateTime(cfgParam);
	}

	@Override
	public void destroy() throws Exception {
		stop();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.setRefreshRateInSeconds(refreshRate);
		start();
	}
}
