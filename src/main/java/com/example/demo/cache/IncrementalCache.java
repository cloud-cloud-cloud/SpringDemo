package com.example.demo.cache;

import com.example.demo.util.Lifecycle;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 *
 * 增量更新机制
 * 每隔refreshRateInSeconds时间，根据updateTime获取增量数据
 * cache以Key-Value形式存放
 * 初次加载时间是动态的，尽量保证集群中节点同时检查更新
 * 目前没有删除机制，若需删除部分配置则需要重启服务
 */
public abstract class IncrementalCache<T extends CacheData> implements Lifecycle {

	private static final Logger logger = LoggerFactory.getLogger(IncrementalCache.class);

	private long newestDataUpdateTime = 0L;

	private static final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor(
			new ThreadFactoryBuilder().setNameFormat("IncrementalCacheThread").setDaemon(true).build()
	);

	private ConcurrentHashMap<String, T> cacheMap = new ConcurrentHashMap<String, T>();

	private int refreshRateInSeconds = 30;

	private List<CacheChangeListener> listeners = new CopyOnWriteArrayList<>();

	private ScheduledFuture<?> scheduledFuture;

	public void setRefreshRateInSeconds(int refreshRateInSeconds) {
		this.refreshRateInSeconds = refreshRateInSeconds;
	}

	@Override
	public void start() {
		if (scheduledFuture == null) {
			incrementalLoad();

			scheduledFuture = ses.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					try {
						incrementalLoad();
					} catch (Exception e) {
						logger.error("incremental load exception", e);
					}
				}
			}, initialDelay(1000L * refreshRateInSeconds), 1000L * refreshRateInSeconds , TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void stop() {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(true);
			scheduledFuture = null;
		}
	}

	protected void incrementalLoad() {

		List<T> forUpdate = doLoadData(newestDataUpdateTime);

		if (forUpdate == null || forUpdate.size() == 0) {
			return;
		}

		long newestUpdateTime = 0;

		for (T d : forUpdate) {
			newestUpdateTime = Math.max(newestUpdateTime, d.lastUpdateTime());

			T t = buildCacheData(d);
			if (t == null) {
				continue;
			}

			T old = cacheMap.put(d.getDataKey(), t);
			try {
				fireListeners(d.getDataKey(), t, old);
			} catch (Exception e) {
				logger.error("cache change listener exception, ", e);
			}

			logger.info("IncrementalCache : {} updated. Key: {}", cacheName(), d.getDataKey());
		}

		if (newestUpdateTime > newestDataUpdateTime) {
			newestDataUpdateTime = newestUpdateTime;
		}
	}

	public void register(CacheChangeListener listener) {
		listeners.add(listener);
	}

	private void fireListeners(String key, T newValue, T oldValue) {
		for (CacheChangeListener listener : listeners) {
			listener.onChange(key, newValue, oldValue);
		}
	}

	protected String cacheName() {
		return this.getClass().getSimpleName();
	}

	protected T buildCacheData(T t) {
		return t;
	}

	protected abstract List<T> doLoadData(long lastUpdateTime);

	public T getCacheData(String dataKey) {
		return cacheMap.get(dataKey);
	}

	public Map<String, T> getAll() {
		return Collections.unmodifiableMap(cacheMap);
	}


	/**
	 * 尽可能保证集群中各节点同时更新配置
	 * 根据当前时间计算出首次执行时间
	 * @return 当前到首次执行时间需要等待的毫秒数
	 */
	private long initialDelay(long rate) {
		long now = System.currentTimeMillis();
		return rate - (now % rate);
	}
}
