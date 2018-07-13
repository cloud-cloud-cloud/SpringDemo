package com.example.demo.config;

import com.example.demo.entity.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by HMa on 2018/7/4.
 */
@Configuration
public class RedisConfig {
	@Autowired
	private RedisProperties redisProperties;

	@Bean
	public RedisClusterConfiguration redisClusterConfiguration() {
		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
		redisClusterConfiguration.setClusterNodes(getRedisNode());
		redisClusterConfiguration.setMaxRedirects(redisProperties.getMaxRedirects());
		return redisClusterConfiguration;
	}

	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
		jedisPoolConfig.setMaxTotal(redisProperties.getMaxTotal());
		jedisPoolConfig.setMinIdle(redisProperties.getMinIdle());
		jedisPoolConfig.setMaxWaitMillis(redisProperties.getMaxWaitMillis());
		jedisPoolConfig.setNumTestsPerEvictionRun(redisProperties.getNumTestsPerEvictionRun());
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(redisProperties.getTimeBetweenEvictionRunsMillis());
		jedisPoolConfig.setTestOnBorrow(redisProperties.isTestOnBorrow());
		return jedisPoolConfig;

	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory(RedisClusterConfiguration clusterConfiguration){
	JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory(clusterConfiguration);
		jedisConnectionFactory.setTimeout(redisProperties.getTimeout());
		return jedisConnectionFactory;
	}


	@Bean
	public RedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory){
	RedisTemplate redisTemplate=new RedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		return redisTemplate;
	}

	/**
	 * 获取redisNode
	 * @return
	 */
	private List<RedisNode> getRedisNode(){
		List<String> nodes=redisProperties.getNodes();
		if(nodes!=null&&nodes.size()>0){
			List<RedisNode> redisNodes=nodes.stream().map(node->{
				String[] ss=node.split(":");
				return new RedisNode(ss[0],Integer.valueOf(ss[1]));
			}).collect(Collectors.toList());
			return redisNodes;
		}
		return new ArrayList<>();
	}

}
