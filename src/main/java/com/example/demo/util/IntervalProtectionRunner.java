package com.example.demo.util;
import java.util.function.Function;

/**
 * Created by HanXiong on 2017/9/8.
 *
 * 保证一段时间之内最多只执行一次
 */
public class IntervalProtectionRunner<T, R> {

	private long lastRunTime = 0;

	private final long interval;

	private final Function<T, R> runnable;

	public IntervalProtectionRunner(long interval, Function<T, R> runnable) {
		this.interval = interval;
		this.runnable = runnable;
	}

	public R run(T t) {
		long now = System.currentTimeMillis();
		synchronized (this) {
			long nextPermitTime = lastRunTime + interval;
			if (now < nextPermitTime) {
				return null;
			}
			lastRunTime = System.currentTimeMillis();
		}
		return runnable.apply(t);
	}

//	public static void main(String[] args) {
//		IntervalProtectionRunner<String, Integer> protectedEmailSender = new IntervalProtectionRunner(1000L * 5, new Function<String, Integer>() {
//
//			@Override
//			public Integer apply(String t) {
//				System.out.println(DateFormatUtils.format(new Date(), "HH:mm:ss") + ":  " + t);
//				return t.length();
//			}
//		});
//
//		IdGenerator idGenerator = new SimpleUniqIdGenerator();
//		for (int i = 0; i < 5; i++) {
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					while (true) {
//
//						protectedEmailSender.run("" +idGenerator.generate());
//
//						try {
//							Thread.sleep(500);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}).start();
//		}
//	}
}
