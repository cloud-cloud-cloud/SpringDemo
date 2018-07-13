package com.example.demo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.UUID;

/**
 * Created by HMa on 2018/6/29.
 * 为了保证最终的一致性，采用分布式锁
 */
public class DistributedLock {
	private final JedisPool jedisPool;

	//todo 1.获取锁的 时候使用setnx加锁，并使用expire命令给加一个超时时间，超过时间自动释放锁。锁的value为一个随机生成的uuid,
	//todo 2.获取锁的 时候还设置一个获取的超时时间，若超过时间则自动放弃锁；
	//todo 3.释放锁的时候通过判断uuid是否是该锁，如果是，则执行delete释放该锁；

	public DistributedLock(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 加锁
	 * @param lockName
	 * @param acquireTimeout
	 * @param timeout
	 * @return
	 */
	public String lockWithTimeout(String lockName, long acquireTimeout, long timeout) {
		Jedis conn = null;
		String retRandomValue = null;
		try {
			conn = jedisPool.getResource();
			String randomValue = UUID.randomUUID().toString();
			String lockKey = "lock:" + lockName;
			int lockExpire = (int) (timeout / 1000);
			long end = System.currentTimeMillis() + acquireTimeout;//获取锁的超时时间，超过这个时间则自动放弃锁

			while (System.currentTimeMillis() < end) {//在获取锁时间内去尝试获取锁
				if (conn.setnx(lockKey, randomValue) == 1) {//当且仅当key不存在时，set一个key为val的字符串，返回1；若key存在，则什么都不做，返回0。
					conn.expire(lockKey, lockExpire);
					retRandomValue = randomValue;
					return retRandomValue;
				}
				if (conn.ttl(lockKey) == -1) {
					//返回-1代表key没有设置超时时间，为key设置一个超时时间
					conn.expire(lockKey, lockExpire);
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

			}
		} catch (JedisException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			}

		}

		return retRandomValue;
	}

	/**
	 * shi
	 * @param lockName
	 * @param setRandomValue
	 * @return
	 */
      public boolean releaseLock(String lockName,String setRandomValue){
		 Jedis conn=null;
		  String lockKey="lock:"+lockName;
		  Boolean retFlag=false;
         try{
			 conn=jedisPool.getResource();
			 while(true){
				 //监控锁，准备开始事务
				 conn.watch(lockKey);
				 if(setRandomValue.equals(conn.get(lockKey))){
					 Transaction transaction=conn.multi();//返回一个事务控制对象
					 transaction.del(lockKey);//释放锁，删除
					 List<Object> results=transaction.exec();//开始执行事务
					 if(results==null){
						 continue;
					 }
					 retFlag=true;
				 }
				 conn.unwatch();
				 break;//跳出循环体
			 }
		 }catch (JedisException e){
			 e.printStackTrace();
		 }finally {
			 if(conn!=null){
				 conn.close();
			 }
		 }
          return retFlag;
	  }



}
