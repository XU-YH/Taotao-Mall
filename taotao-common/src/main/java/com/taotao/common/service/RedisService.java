package com.taotao.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisService {

	@Autowired(required = false) //从Spring容器中查找bean，找到则注入，否则不注入
	private ShardedJedisPool shardedJedisPool;

	private <T> T excute(Function<T, ShardedJedis> fun) {
		// 定义集群连接池
		ShardedJedis shardedJedis = null;
		try {
			// 从连接池中获取到jedis分片对象
			shardedJedis = shardedJedisPool.getResource();
			return fun.callback(shardedJedis);
		} finally {
			if (null != shardedJedis) {
				// 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
				shardedJedis.close();
			}
		}
	}

	/**
	 * 执行set操作
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value) {
		return this.excute(new Function<String, ShardedJedis>() {

			@Override
			public String callback(ShardedJedis e) {
				return e.set(key, value);
			}
		});
	}

	/**
	 * 执行get操作
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		return this.excute(new Function<String, ShardedJedis>() {

			@Override
			public String callback(ShardedJedis e) {
				return e.get(key);
			}
		});
	}

	/**
	 * 执行del操作
	 * 
	 * @param key
	 * @return
	 */
	public Long del(final String key) {
		return this.excute(new Function<Long, ShardedJedis>() {

			@Override
			public Long callback(ShardedJedis e) {
				return e.del(key);
			}
		});
	}

	/**
	 * 设置生存时间，单位为秒
	 * 
	 * @param key
	 * @return
	 */
	public Long expire(String key, Integer seconds) {
		return this.excute(new Function<Long, ShardedJedis>() {

			@Override
			public Long callback(ShardedJedis e) {
				return e.expire(key, seconds);
			}
		});
	}

	/**
	 * 执行set操作并设置生存时间，单位为秒
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String set(String key, String value, Integer seconds) {
		return this.excute(new Function<String, ShardedJedis>() {

			@Override
			public String callback(ShardedJedis e) {
				String str = e.set(key, value);
				e.expire(key, seconds);
				return str;
			}
		});
	}

}
