package com.example.demo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by HMa on 2018/7/4.
 */
@SuppressWarnings("unchecked")
@Component
 public class SpringUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}

	// getBean by  @Resouce
	public static <T> T getBean(String beanName) {
		if(applicationContext.containsBean(beanName)){
			return (T) applicationContext.getBean(beanName);
		}else{
			return null;
		}
	}

	// getbeanByTean   @Authwire
	public static <T> Map<String, T> getBeansOfType(Class<T> baseType){
		return applicationContext.getBeansOfType(baseType);
	}
}