package com.example.demo.distributeLockDemo;

import com.example.demo.SpringBootDemoApplication;
import com.example.demo.config.RedisLock;

import com.example.demo.util.SpringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.CountDownLatch;

/**
 * Created by HMa on 2018/7/4.
 *
 * spring boot注入 的时候，如果对应的启动类application和注入的对象不在同一个包或者子包下面就不无法bean
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootDemoApplication.class)
@WebAppConfiguration
public class TestRedis {
	private CountDownLatch countDownLatch=new CountDownLatch(2);

	@Test
	public void TestRedisLock()throws InterruptedException{
		new Thread(new Runnable() {
			@Override
			public void run() {
				RedisLock lock = ((RedisLock) SpringUtil.getBean("redisLock")).newInstance("test");
				if (lock.lock()) {
					System.out.println("work1获得锁");
					System.out.println("work1 工作15s...");
					try {
						Thread.sleep(15000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("work1完成工作，释放锁");
					lock.unlock();
				}
				countDownLatch.countDown();
			}
		},"work1").start();


			new Thread(new Runnable() {
			@Override
			public void run() {
				RedisLock redisLock = ((RedisLock) SpringUtil.getBean("redisLock")).newInstance("test");;
				if(redisLock.lock()){
					System.out.println("work2获取锁");
					System.out.println("work2工作。。。。。");
					try{
						Thread.sleep(5000);
					}catch (InterruptedException e){
						e.printStackTrace();
					}
					System.out.println("work2工作完成释放锁");
					redisLock.unlock();
				}
				countDownLatch.countDown();

			}
		},"work2").start();
		countDownLatch.await();//等待两个线程完成，才完成主线程
	}
}
