package edu.sjsu.LPOS.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTokenStoreService {

	private static final long EXPIRE_IN_HOURS = 48; 
	
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	public void set(String username, String token, long expires, TimeUnit unit) {
		redisTemplate.opsForValue().set(username, token, expires, unit);
	}
	
	public void set(String username, String token) {
		redisTemplate.opsForValue().set(username, token, EXPIRE_IN_HOURS, TimeUnit.HOURS);
	}
	
	public String get(String username) {
		if (username == null) {
			return null;
		}
		String token = redisTemplate.opsForValue().get(username);
		return token;
	}
}
