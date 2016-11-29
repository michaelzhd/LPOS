package edu.sjsu.LPOS.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisStoreService {

	
	@Autowired RedisTemplate<String, String> redisTemplate;
	
	public void setToken(String username, String token, long expires, TimeUnit unit) {
		redisTemplate.opsForValue().set(username, token, expires, unit);
	}
	
	public String get(String username) {
		if (username == null) {
			return null;
		}
		String token = redisTemplate.opsForValue().get(username);
		return token;
	}
	
	public void setVerificationCode(String codeKey, String code, long expires, TimeUnit unit) {
		redisTemplate.opsForValue().set(codeKey, code, expires, unit);
	}
	
	public String getVerificationCode(String codeKey) {
		return redisTemplate.opsForValue().get(codeKey);
	}

}
