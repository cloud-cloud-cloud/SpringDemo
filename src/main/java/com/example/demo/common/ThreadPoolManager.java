package com.example.demo.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.demo.lib.current.MDCInheritanceThreadPoolTaskExecutor;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by HMa on 2018/8/24.
 * 用来管理线程池的类
 */
@Component
public class ThreadPoolManager {
	private static final String DEFALUT_POOL_NAME="default";//设置默认的线程池，
	private Map<String,Executor> threadPoolMap;

	public ThreadPoolManager(){
		try {
			URL url= Resources.getResource("threadPoolDefinition.json");//获取资源,不同类型的请求可以用不同的线程池，避免一个线程池满了，阻塞后面其他请求
			String content=Resources.toString(url, Charsets.UTF_8);
            List<ThreadPoolDefinition> poolDefinitionList= JSON.parseObject(content,new TypeReference<List<ThreadPoolDefinition>>(){});
			ImmutableMap.Builder<String,Executor> builder=ImmutableMap.builder();
			for(ThreadPoolDefinition t:poolDefinitionList){
				builder.put(t.getName(),createThreadPool(t));
			}
		} catch (IOException e) {
			throw new IllegalStateException("load threadPoolDefinition failed", e);
		}
	}


	public Executor getPool(String s) {
		return threadPoolMap.get(s);
	}

	public Executor getDefaultPool() {
		return threadPoolMap.get(DEFALUT_POOL_NAME);
	}

	private Executor createThreadPool(ThreadPoolDefinition t){
		MDCInheritanceThreadPoolTaskExecutor es=new MDCInheritanceThreadPoolTaskExecutor();//记录线程上下文执行器
		es.setCorePoolSize(t.getCorePoolSize());
		es.setMaxPoolSize(t.getMaxPoolSize());
		es.setQueueCapacity(t.getQueueSize());
		es.setKeepAliveSeconds(60);
		es.setThreadNamePrefix("p-"+t.getName());
		es.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		es.initialize();
		return es;

	}

	public static class ThreadPoolDefinition {
		private String name;

		private int corePoolSize;

		private int queueSize;

		private int maxPoolSize;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getCorePoolSize() {
			return corePoolSize;
		}

		public void setCorePoolSize(int corePoolSize) {
			this.corePoolSize = corePoolSize;
		}

		public int getQueueSize() {
			return queueSize;
		}

		public void setQueueSize(int queueSize) {
			this.queueSize = queueSize;
		}

		public int getMaxPoolSize() {
			return maxPoolSize;
		}

		public void setMaxPoolSize(int maxPoolSize) {
			this.maxPoolSize = maxPoolSize;
		}
	}

}
