package com.example.demo.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class SpringContextHelper implements BeanFactoryAware {

	private static CountDownLatch countDownLatch = new CountDownLatch(1);

	private static BeanFactory beanFactory;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public static <T> T getBean(String beanName) {
		return (T) beanFactory.getBean(beanName);
	} 

	public static <T> T getBean(Class beanType) {
		return (T) beanFactory.getBean(beanType);
	}

	/**
	 * 异步线程可能会调用
	 * @throws InterruptedException
	 */
	public static void waitForSpringContextReady() throws InterruptedException {
		countDownLatch.await();
	}

	public static void setSpringContextReady() {
		countDownLatch.countDown();
	}
}