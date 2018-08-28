package com.example.demo.lib.current;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by HanXiong on 2017/9/5.
 */
public class RetryTaskExecutor {

	private AtomicBoolean started = new AtomicBoolean(false);

	private ScheduledExecutorService ses;

	private int poolSize = 5;

	private void canncelCurrentTask(ScheduledFuture<?> sft,boolean canncelType) {
		//canncelType   true:立即终止 ;false:执行完后终止
		sft.cancel(canncelType);
	}
	
	public void start() {
		if (started.compareAndSet(false, true)) {
			ses = new ScheduledThreadPoolExecutor(poolSize);
		}
	}

	public void stop() {
		if (started.compareAndSet(true, false)) {
			if (ses != null) {
				ses.shutdown();
				ses = null;
			}
		}
	}

	public void setThreadPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public <V> V doTask(final Callable<V> task, final long retryGap) throws TimeoutException, InterruptedException, ExecutionException {
		return doTask(task, retryGap, 0);
	}

	public <V> V doTask(final Callable<V> task, final long retryGap, final long maxWaitingTime) throws TimeoutException,
			InterruptedException, ExecutionException {

		final long deadline = (maxWaitingTime <= 0) ? Long.MAX_VALUE : maxWaitingTime + System.currentTimeMillis();

		CountDownLatch latch = new CountDownLatch(1);

		QueryTask<V> queryTask = new QueryTask<V>(task, latch, retryGap, deadline);
		ScheduledFuture<?> sft = ses.schedule(queryTask, 0, TimeUnit.MILLISECONDS);

		if (!latch.await(maxWaitingTime, TimeUnit.MILLISECONDS)) {
			canncelCurrentTask(sft, true);
			throw new TimeoutException();
		}

		try {
			return queryTask.getResult();
		} catch (Exception e) {
			if (e instanceof TimeoutException) {
				throw (TimeoutException) e;
			} else {
				throw new ExecutionException(e);
			}
		}
	}


	private class QueryTask<T> implements Runnable {

		private CountDownLatch latch;

		private Callable<T> callable;

		private long retryGap;

		private long deadline;

		private volatile ScheduledFuture<?> currentFuture = null;

		private volatile T result;

		private Exception e;

		public QueryTask(Callable<T> callable, CountDownLatch latch, long retryGap, long deadline) {
			this.callable = callable;
			this.retryGap = retryGap;
			this.deadline = deadline;
			this.latch = latch;
			this.result = null;
			this.e = null;
		}

		@Override
		public void run() {
			try {
				
				// 超时
				checkTimeout(System.currentTimeMillis());
				T t = callable.call();

				if (t != null) {
					this.result = t;
					latch.countDown();
					return;
				} else {
					// 下次必然超时，直接报超时
					checkTimeout(System.currentTimeMillis() + retryGap);

					currentFuture = ses.schedule(this, retryGap, TimeUnit.MILLISECONDS);
				}
			} catch (Exception e) {
				this.e = e;
				if (currentFuture != null) {
					currentFuture.cancel(true);
				}
				latch.countDown();
			}
		}

		private void checkTimeout(long time) throws TimeoutException {
			if (time > deadline) {
				throw new TimeoutException();
			}
		}

		public T getResult() throws Exception {
			if (e != null) {
				throw e;
			}
			if (currentFuture != null) {
				currentFuture.cancel(true);
			}
			return result;
		}
	}
}
