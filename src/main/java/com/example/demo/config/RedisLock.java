package com.example.demo.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by HMa on 2018/7/4.
 * 分布式锁
 */
@Component
public class RedisLock {

	/**
	 *
	 * 1、获得当前系统时间，计算锁的到期时间
	 2、setNx操作，加锁
	 3、如果，加锁成功，设置锁的到期时间，返回true；取锁失败，取出当前锁的value(到期时间)
	 4、如果value不为空而且小于当前系统时间，进行getAndSet操作，重新设置value，并取出旧value；否则，等待间隔时间后，重复步骤2；
	 5、如果步骤3和4取出的value一样，加锁成功，设置锁的到期时间，返回true；否则，别人加锁成功，恢复锁的value，等待间隔时间后，重复步骤2。
	 */

	private static final Logger log = LoggerFactory.getLogger(RedisLock.class);
	/* 默认锁的有效时间30s */
	private static final int DEFAULT_LOCK_EXPIRSE_MILL_SECONDS = 30 * 1000;
	/* 默认请求锁等待超时时间10s */
	private static final int DEFAULT_LOCK_WAIT_DEFAULT_TIME_OUT = 10 * 1000;
	/* 默认的轮询获取锁的间隔时间 */
	private static final int DEFAULT_LOOP_WAIT_TIME = 150;
	/* 锁的key前缀 */
	private static final String LOCK_PREFIX = "LOCK_";

	/* 是否获得锁的标志 */
	private boolean lock = false;
	/* 锁的key */
	private String lockKey;
	/* 锁的有效时间(ms) */
	private int lockExpirseTimeout;
	/* 请求锁的阻塞时间(ms) */
	private int lockWaitTimeout;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public String getLockKey() {
		return lockKey;
	}

	public void setLockKey(String lockKey) {
		this.lockKey = LOCK_PREFIX + lockKey;
	}

	public int getLockExpirseTimeout() {
		return lockExpirseTimeout;
	}

	public void setLockExpirseTimeout(int lockExpirseTimeout) {
		this.lockExpirseTimeout = lockExpirseTimeout;
	}

	public int getLockWaitTimeout() {
		return lockWaitTimeout;
	}

	public void setLockWaitTimeout(int lockWaitTimeout) {
		this.lockWaitTimeout = lockWaitTimeout;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public RedisLock() {
	}

	/**
	 *
	 * @param lockKey  key
	 * @param lockExpirseTimeout
	 * @param lockWaitTimeout
	 */
	public RedisLock(String lockKey, int lockExpirseTimeout, int lockWaitTimeout) {
		this.lockKey = LOCK_PREFIX + lockKey;
		this.lockExpirseTimeout = lockExpirseTimeout;
		this.lockWaitTimeout = lockWaitTimeout;
	}

	public RedisLock newInstance(String lockKey) {
		RedisLock redisLock = new RedisLock(lockKey, DEFAULT_LOCK_EXPIRSE_MILL_SECONDS, DEFAULT_LOCK_WAIT_DEFAULT_TIME_OUT);
		redisLock.setRedisTemplate(this.redisTemplate);
		return redisLock;
	}

	public RedisLock newInstance(String lockKey, int lockExpirseTimeout, int lockWaitTimeout) {
		if (lockExpirseTimeout == 0 || lockWaitTimeout == 0) {
			lockExpirseTimeout = DEFAULT_LOCK_EXPIRSE_MILL_SECONDS;
			lockWaitTimeout = DEFAULT_LOCK_WAIT_DEFAULT_TIME_OUT;
		}
		RedisLock redisLock = new RedisLock(lockKey, lockExpirseTimeout, lockWaitTimeout);
		redisLock.setRedisTemplate(this.redisTemplate);
		return redisLock;
	}

	public boolean setIfAbsent(String expirseTimeStr) {
		// setIfAbsent通过jedis的setNx实现
		return this.redisTemplate.opsForValue().setIfAbsent(this.lockKey, expirseTimeStr);
	}

	public String getAndSet(String expiresTimeStr) {
		// 获取原来的值，并设置新的值，原子操作
		return (String) this.redisTemplate.opsForValue().getAndSet(this.lockKey, expiresTimeStr);
	}

	/**
	 * 1、获得当前系统时间，计算锁的到期时间
	 * 2、setNx操作，加锁
	 * 3、如果，加锁成功，设置锁的到期时间，返回true；取锁失败，取出当前锁的value(到期时间)
	 * 4、如果value不为空而且小于当前系统时间，进行getAndSet操作，重新设置value，并取出旧value；否则，等待间隔时间后，重复步骤2；
	 * 5、如果步骤3和4取出的value一样，加锁成功，设置锁的到期时间，返回true；否则，别人加锁成功，恢复锁的value，等待间隔时间后，重复步骤2。
	 */
	public boolean lock() {
		log.info("{}-----尝试获取锁...", Thread.currentThread().getName());
		int lockWaitMillSeconds = this.lockWaitTimeout;
		// key 的值，表示key的到期时间
		String redisValue = String.valueOf(System.currentTimeMillis() + this.lockExpirseTimeout);//锁的到期时间
		while (lockWaitMillSeconds > 0) {//获取锁的等待时间，超时放弃
			lock = setIfAbsent(redisValue);
			if (lock) {
				// 拿到锁,设置锁的有效期,这里可能因为故障没有被执行，锁会一直存在，这时就需要value的有效期去判断锁是否失效
				this.redisTemplate.expire(this.lockKey, lockExpirseTimeout, TimeUnit.MILLISECONDS);
				log.info("{}-----获得锁", Thread.currentThread().getName());
				return lock;
			} else {
				// 锁存在，判断锁有没有过期
				String oldValue = (String) this.redisTemplate.opsForValue().get(this.lockKey);
				if (StringUtils.isNotEmpty(oldValue) && Long.parseLong(oldValue) < System.currentTimeMillis()) {
					// 锁的到期时间小于当前时间，说明锁已失效, 修改value，获得锁
					String currentRedisValue = getAndSet(String.valueOf(lockExpirseTimeout + System.currentTimeMillis()));
					// 如果两个值不相等，说明有另外一个线程拿到了锁，阻塞
					if (currentRedisValue.equals(oldValue)) {
						// 如果修改的锁的有效期之前没被其他线程修改，则获得锁, 设置锁的超时时间
						redisTemplate.expire(this.lockKey, lockExpirseTimeout, TimeUnit.MILLISECONDS);
						log.info("{}-----获得锁", Thread.currentThread().getName());
						this.lock = true;
						return this.lock;
					} else {
						// 有另外一个线程获得了这个超时的锁,不修改锁的value
						redisTemplate.opsForValue().set(this.lockKey, currentRedisValue);
					}
				}
			}
			// 减掉固定轮询获取锁的间隔时间
			lockWaitMillSeconds -= DEFAULT_LOOP_WAIT_TIME;
			try {
				log.info("{}-----等待{}ms后，再尝试获取锁...", Thread.currentThread().getName(), DEFAULT_LOOP_WAIT_TIME);
				// 取锁失败时，应该在随机延时后进行重试，避免不同客户端同时重试导致谁都无法拿到锁的情况出现,也可以采用等待队列的方式
				Thread.sleep(DEFAULT_LOOP_WAIT_TIME);
			} catch (InterruptedException e) {
				log.error("redis 同步锁出现未知异常", e);
			}
		}
		log.info("{}-----请求锁超时，获得锁失败", Thread.currentThread().getName());
		return false;
	}
	public void unlock() {
		if (lock) {
			this.redisTemplate.delete(this.lockKey);
			this.lock = false;
		}
	}
}
