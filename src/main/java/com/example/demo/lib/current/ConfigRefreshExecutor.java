package com.example.demo.lib.current;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ConfigRefreshExecutor {

	private ScheduledExecutorService ses;

	public ConfigRefreshExecutor() {
		this(1);
	}

	public ConfigRefreshExecutor(int poolSize) {
		ses = new ScheduledThreadPoolExecutor(poolSize, new ThreadFactoryBuilder()
				.setNameFormat("ConfigRefreshExecutor-%d")
				.setDaemon(true)
				.build());
	}

	public ScheduledFuture<?> submit(Runnable refreshTask, long delayTimeInMills) {
		return ses.scheduleAtFixedRate(refreshTask, initialDelay(delayTimeInMills), delayTimeInMills, TimeUnit.MILLISECONDS);
	}

	private long initialDelay(long delayTimeInMills) {
		long now = System.currentTimeMillis();
		return delayTimeInMills - (now % delayTimeInMills);
	}
}
