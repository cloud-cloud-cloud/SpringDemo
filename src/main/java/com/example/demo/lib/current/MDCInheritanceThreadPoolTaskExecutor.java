package com.example.demo.lib.current;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class MDCInheritanceThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

	@Override
	public void execute(Runnable task) {
		super.execute(new MDCInheritanceRunnable(task));
	}

	@Override
	public void execute(Runnable task, long startTimeout) {
		super.execute(new MDCInheritanceRunnable(task));
	}

	@Override
	public Future<?> submit(Runnable task) {
		return super.submit(new MDCInheritanceRunnable(task));
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return super.submit(new MDCInheritanceCallable<T>(task));
	}

	@Override
	public ListenableFuture<?> submitListenable(Runnable task) {
		return super.submitListenable(new MDCInheritanceRunnable(task));
	}

	@Override
	public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
		return super.submitListenable(new MDCInheritanceCallable<T>(task));
	}
}
