package com.example.demo.lib.current;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolFactory {
	
	private static Logger logger = LoggerFactory.getLogger(ThreadPoolFactory.class);

	// 存放所有的线程池
	private static Map<String, ThreadPoolExecutor> POOL_MAP = new HashMap<String, ThreadPoolExecutor>();

	// 固定线程数目的线程池-->核心线程池大小
	private static final int DEFAULT_CORE_POOL_SIZE = 5;

	// 固定线程数目的线程池-->最大线程池大小
	private static final int DEFAULT_MAXIMUM_POOL_SIZE = 10;

	// 固定线程数目的线程池-->空闲线程最大存活时间
	private static final long DEFAULT_KEEP_ALIVE_TIME = 60L;

	// 固定线程数目的线程池-->时间单位
	private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.SECONDS;

	// 固定线程数目的线程池-->阻塞任务队列
	private static final LinkedBlockingQueue<Runnable> DEFAULT_WORK_QUEUE = new LinkedBlockingQueue<Runnable>(
			20);

	public static void init(Document document) {
		
		logger.info("线程池初始化开始...");
		
		ThreadPoolExecutor defaultPool = null;
		ThreadPoolExecutor singledPool = null;
		if (document != null) {
			NodeList defaultNode = document.getElementsByTagName("pool");
			if (defaultNode != null && defaultNode.getLength() > 0) {
				for (int i = 0; i < defaultNode.getLength(); i++) {
					Element poolElement = (Element) defaultNode.item(i);
					System.out.println("node value:"
							+ poolElement.getAttributeNode("name").getNodeValue());
					if ("default".equals(poolElement.getAttributeNode("name")
							.getNodeValue())) {
						//核心线程大小
						String defaultCorePoolSizeStr = poolElement
								.getElementsByTagName("corePoolSize").item(0)
								.getFirstChild().getNodeValue();
						int defaultCorePoolSize = Integer.parseInt(defaultCorePoolSizeStr==null?"5":defaultCorePoolSizeStr);
						
						//最大线程数
						String defaultMaxPoolSizeStr = poolElement
								.getElementsByTagName("maxPoolSize").item(0)
								.getFirstChild().getNodeValue();
						int defaultMaxPoolSize = Integer.parseInt(defaultMaxPoolSizeStr==null?"10":defaultMaxPoolSizeStr);
						
						//有效时间
						String defaultKeepAliveTimeStr = poolElement
								.getElementsByTagName("keepAliveTime").item(0)
								.getFirstChild().getNodeValue();
						long defaultKeepAliveTime = Long.parseLong(defaultKeepAliveTimeStr==null?"60":defaultKeepAliveTimeStr);
						
						//队列长度
						String defaultWorkQueueSizeStr = poolElement
								.getElementsByTagName("workQueueSize").item(0)
								.getFirstChild().getNodeValue();
						int defaultWorkQueueSize = Integer.parseInt(defaultWorkQueueSizeStr==null?"20":defaultWorkQueueSizeStr);

						defaultPool = new ThreadPoolExecutor(
								defaultCorePoolSize, defaultMaxPoolSize,
								defaultKeepAliveTime, DEFAULT_TIME_UNIT,
								new LinkedBlockingQueue<Runnable>(
										defaultWorkQueueSize),
								new CustomThreadFactory(),
								new CustomRejectedExecutionHandler());
					} else if ("singled".equals(poolElement
							.getAttributeNode("name").getNodeValue())) {
						int singledWorkQueueSize = Integer.parseInt(poolElement
								.getElementsByTagName("workQueueSize").item(0)
								.getFirstChild().getNodeValue());

						singledPool = new ThreadPoolExecutor(1, 1, 0L,
								DEFAULT_TIME_UNIT,
								new LinkedBlockingQueue<Runnable>(
										singledWorkQueueSize),
								new CustomThreadFactory(),
								new CustomRejectedExecutionHandler());
					}
				}
			}

		} else {
			defaultPool = new ThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE,
					DEFAULT_MAXIMUM_POOL_SIZE, DEFAULT_KEEP_ALIVE_TIME,
					DEFAULT_TIME_UNIT, DEFAULT_WORK_QUEUE,
					new CustomThreadFactory(),
					new CustomRejectedExecutionHandler());

			singledPool = new ThreadPoolExecutor(1, 1, 0L, DEFAULT_TIME_UNIT,
					new LinkedBlockingQueue<Runnable>(10),
					new CustomThreadFactory(),
					new CustomRejectedExecutionHandler());
		}


		POOL_MAP.put(PoolType.DEFAULT.toString(), defaultPool);
		POOL_MAP.put(PoolType.SINGLED.toString(), singledPool);

		logger.info("defaultPool:" + defaultPool);
		logger.info("singledPool:" + singledPool);
		logger.info("线程池初始化结束...");
	}

	/**
	 * @Title: getInstance
	 * @Description: 获得默认的线程池
	 * @param @return
	 * @return ThreadPoolExecutor @throws
	 */
	public static ThreadPoolExecutor getInstance() {
		ThreadPoolExecutor pool = POOL_MAP.get(PoolType.DEFAULT.toString());
		synchronized (ThreadPoolFactory.class) {
			if (pool == null) {
				pool = new ThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE,
						DEFAULT_MAXIMUM_POOL_SIZE, DEFAULT_KEEP_ALIVE_TIME,
						DEFAULT_TIME_UNIT, DEFAULT_WORK_QUEUE,
						new CustomThreadFactory(),
						new CustomRejectedExecutionHandler());
				POOL_MAP.put(PoolType.DEFAULT.toString(), pool);
			}
		}
		return pool;
	}

	/**
	 * @Title: getInstance
	 * @Description: 获得默认的线程池
	 * @param @return
	 * @return ThreadPoolExecutor @throws
	 */
	public static ThreadPoolExecutor getInstance(PoolType poolType) {
		ThreadPoolExecutor pool = POOL_MAP.get(poolType.toString());
		synchronized (ThreadPoolFactory.class) {
			if (pool == null) {
				if (PoolType.DEFAULT.toString().equals(poolType.toString())) {
					pool = new ThreadPoolExecutor(DEFAULT_CORE_POOL_SIZE,
							DEFAULT_MAXIMUM_POOL_SIZE, DEFAULT_KEEP_ALIVE_TIME,
							DEFAULT_TIME_UNIT, DEFAULT_WORK_QUEUE,
							new CustomThreadFactory(),
							new CustomRejectedExecutionHandler());
				} else if (PoolType.SINGLED.toString().equals(
						poolType.toString())) {
					pool = new ThreadPoolExecutor(1, 1, 0L, DEFAULT_TIME_UNIT,
							new LinkedBlockingQueue<Runnable>(10),
							new CustomThreadFactory(),
							new CustomRejectedExecutionHandler());
				}

				POOL_MAP.put(poolType.toString(), pool);
			}
		}
		return pool;
	}

	/**
	 * @Title: destroyThreadPool
	 * @Description: 销毁所有的线程池
	 * @param
	 * @return void
	 * @throws
	 */
	public static void destroyAll() {

		logger.info("销毁线程池开始...");

		PoolType[] poolTypes = PoolType.values();
		if (poolTypes != null && poolTypes.length > 0) {
			for (int i = 0; i < poolTypes.length; i++) {
				if (POOL_MAP.get(poolTypes[i]) != null) {
					POOL_MAP.get(poolTypes[i]).shutdownNow();
				}

			}
		}
		
		logger.info("销毁线程池结束...");
	}
}
